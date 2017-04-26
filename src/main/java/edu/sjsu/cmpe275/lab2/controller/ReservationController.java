package edu.sjsu.cmpe275.lab2.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentCollectionConverter;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.model.Views;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;

/**
 * @author Imran
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

	final static Logger logger = Logger.getLogger(FlightController.class);

	@Autowired
	FlightService flightService;

	@Autowired
	PassengerService passengerService;
	
	@Autowired
	ReservationService reservationService;

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	public ResponseEntity<?> makeReservation(
			@RequestParam("passengerId") Long passengerId,
			@RequestParam("flightList") List<String> flightNumbers
			) throws JsonProcessingException {

		Passenger passenger = passengerService.getById(passengerId);
		if (passenger == null) {
			String errMsg = "Sorry, the requested passenger with id " + passengerId + " does not exists";
			logger.debug("Sorry, the requested passenger with id " + passengerId + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);			
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders,HttpStatus.NOT_FOUND);
		}
		
		List<Flight> flights = new ArrayList<Flight>();
		for(String flightNumber : flightNumbers){
			Flight flight = flightService.getById(flightNumber);
			if (flight == null) {
				String errMsg = "Sorry, the requested flight with id " + flightNumber + " does not exists";
				logger.debug("Sorry, the requested flight with id " + flightNumber + " does not exists");
				ErrorJSON err = new ErrorJSON(errMsg);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(MediaType.APPLICATION_JSON);			
				return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders,HttpStatus.NOT_FOUND);
			}
			
			if(flight.getSeatsLeft()<=0)
			{
				String errMsg = "The total amount of passengers can not exceed the capacity of the reserved plane. Flight is full!";
				ErrorJSON err = new ErrorJSON(errMsg);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
				return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders,HttpStatus.NOT_FOUND);
			}
			else{
				flight.setSeatsLeft(flight.getPlane().getCapacity()-1);
				flights.add(flight);
			}
		}
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		int totalPrice = 0;
		for(Flight flight : flights){
			totalPrice += flight.getPrice();
		}
		reservation.setPrice(totalPrice);
		reservation.setFlights(flights);
		reservationService.save(reservation);
		logger.debug("Created reservation " + reservation.getOrdernumber());
		
		XStream xs = new XStream();
		xs.registerConverter(new HibernatePersistentCollectionConverter(xs.getMapper()));
		xs.alias("passenger", Passenger.class);
		xs.alias("flight", Flight.class);
		xs.alias("reservation", Reservation.class);
		
		
		xs.omitField(Passenger.class, "reservations");
		xs.omitField(Flight.class, "passengers");
		xs.omitField(Flight.class, "reservations");
		xs.omitField(Passenger.class, "flights");
		
		  //http://xstream.10960.n7.nabble.com/HibernateCollectionConverter-question-td1620.html
		xs.addDefaultImplementation(PersistentBag.class, List.class);
		xs.addDefaultImplementation(Timestamp.class, Date.class);
	
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<>(xs.toXML(reservation),responseHeaders, HttpStatus.CREATED);
	}
	
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.GET)
	public ResponseEntity<?> getReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exists";
			logger.debug("Reserveration with number " + ordernumber + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders, HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Flight: " + reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exists";
			logger.debug("Reserveration with number " + ordernumber + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders, HttpStatus.NOT_FOUND);
			
		} else {
			reservationService.delete(ordernumber);
			String errMsg = "Reservation with number " + ordernumber + " is canceled successfully";
			logger.debug("Reservation with number " + ordernumber + " is canceled successfully");
			ErrorJSON err = new ErrorJSON(errMsg);			
			JSONObject jsonVal = new JSONObject(err.getSuccessfulMsg());
			String xmlVal = XML.toString(jsonVal);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
			return new ResponseEntity<>(xmlVal,responseHeaders,HttpStatus.OK);
		}
	}

	
}
