package edu.sjsu.cmpe275.lab2.controller;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentCollectionConverter;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.model.Views;
import edu.sjsu.cmpe275.lab2.service.PassengerService;

/**
 * 
 * @author Imran
 *
 */

@RestController
@RequestMapping("/passenger")
public class PassengerController {

	final static Logger logger = Logger.getLogger(PassengerController.class);

	@Autowired
	PassengerService passengerService;

	@JsonView(Views.ProjectRelevantFieldsInPassenger.class)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addPassenger(
			@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName,
			@RequestParam("age") int age,
			@RequestParam("gender") String gender,
			@RequestParam("phone") String phone) throws JsonProcessingException {
		
		if(passengerService.getByPhone(phone).size()==0)
		{
			Passenger passenger = new Passenger(firstName,lastName,age,gender,phone);
			passenger.setReservations(Collections.emptyList());
			try{
				passengerService.save(passenger);
				logger.debug("Added:: " + passenger);
				return new ResponseEntity<Passenger>(passenger, HttpStatus.CREATED);
			}
			catch(DataAccessException e)
			{
				return ControllerUtil.sendBadRequest(e.getMessage(), HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			String errMsg = "Another passenger with the same phone number already exists.";
			HttpStatus status = HttpStatus.BAD_REQUEST;
			return ControllerUtil.sendBadRequest(errMsg, status);
		}
	}

	@JsonView(Views.ProjectRelevantFieldsInPassenger.class)
	@RequestMapping(value = "/{id}",method = RequestMethod.PUT)
	public ResponseEntity<?> updatePassenger(
			@PathVariable("id") Long id,
			@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName,
			@RequestParam("age") int age,
			@RequestParam("gender") String gender,
			@RequestParam("phone") String phone) throws JsonProcessingException{
		
		Passenger existingPassenger = passengerService.getById(id);
		if (existingPassenger == null) {
			String errMsg = "Sorry, the requested passenger with id " + id + " does not exists";
			logger.debug("Sorry, the requested passenger with id " + id + " does not exists");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
	
		}
	
		List<Passenger> passenger = passengerService.getByPhone(phone);
        if(passenger.size()!=0 && passenger.get(0).getId()!=existingPassenger.getId()){
			String errMsg = "Another passenger with the same phone number already exists.";
			logger.debug("Another passenger with the same phone number already exists.");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);	
		}
		else
		{
			existingPassenger.setAge(age);
			existingPassenger.setFirstname(firstName);
			existingPassenger.setLastname(lastName);
			existingPassenger.setGender(gender);
			existingPassenger.setPhone(phone);
			passengerService.save(existingPassenger);
			return new ResponseEntity<Passenger>(existingPassenger, HttpStatus.OK);
		}
	}

	@JsonView(Views.ProjectRelevantFieldsInPassenger.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,produces={MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> getPassenger(
			@PathVariable("id") Long id,
			@RequestParam(value = "xml", required = false) boolean xml) throws JsonProcessingException {
		
		Passenger passenger = passengerService.getById(id);
		if (passenger == null) {
			String errMsg = "Sorry, the requested passenger with id " + id + " does not exists";
			logger.debug("Sorry, the requested passenger with id " + id + " does not exists");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
		}

		HttpHeaders responseHeaders;
		if(xml)
		{
			XStream xs = new XStream();
			xs.registerConverter(new HibernatePersistentCollectionConverter(xs.getMapper()));
			xs.setMode(XStream.NO_REFERENCES);
			xs.alias("passenger", Passenger.class);
			xs.alias("flight", Flight.class);
			xs.alias("reservation", Reservation.class);
			
			  //http://xstream.10960.n7.nabble.com/HibernateCollectionConverter-question-td1620.html
			xs.addDefaultImplementation(PersistentBag.class, List.class);
			xs.addDefaultImplementation(Timestamp.class, Date.class);
			  
			xs.omitField(Reservation.class, "passenger");
			xs.omitField(Flight.class, "reservations");
			xs.omitField(Flight.class, "passengers");
			xs.omitField(Passenger.class, "flights");
			
			responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
		    return new ResponseEntity<>(xs.toXML(passenger),responseHeaders, HttpStatus.OK);
		}
		else
		{
			responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(passenger,responseHeaders, HttpStatus.OK);
		}
				
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePassenger(@PathVariable("id") Long id) throws JsonProcessingException {
		Passenger passenger = passengerService.getById(id);
		if (passenger == null) {
			String errMsg = "Passenger with id " + id + " does not exists";
			logger.debug("Passenger with id " + id + " does not exists");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
			
		} else {
			List<Reservation> reservations = passenger.getReservations();
			for(Reservation reservation : reservations){
				for(Flight flight : reservation.getFlights()){
					flight.setSeatsLeft(flight.getSeatsLeft() + 1);
				}
			}
			passengerService.delete(id);
			String successMsg = "Passenger with id " + id + " is deleted successfully";
			logger.debug("Passenger with id " + id + " is deleted successfully");
			return ControllerUtil.sendSuccess(successMsg);
		}
	}
}
