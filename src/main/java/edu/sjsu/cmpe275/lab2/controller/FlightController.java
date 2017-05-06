package edu.sjsu.cmpe275.lab2.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentCollectionConverter;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Plane;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.model.Views;
import edu.sjsu.cmpe275.lab2.service.FlightService;


/**
* <h1>Flight Endpoints</h1>
* The flight controller class provides the REST endpoints
* to map the basic GET,POST,PUT and DELETE request
* and its corresponding operations
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-24
*/ 

@RestController
@RequestMapping("/flight")
public class FlightController {

	final static Logger logger = Logger.getLogger(FlightController.class);

	@Autowired
	FlightService flightService;

	/**
	 * @param flightNumber
	 * @param price
	 * @param from
	 * @param to
	 * @param departureTime
	 * @param arrivalTime
	 * @param description
	 * @param capacity
	 * @param model
	 * @param manufacturer
	 * @param yearOfManufacture
	 * @return error or JSON response of the newly created flight
	 * @throws JsonProcessingException
	 */
	@JsonView(Views.ProjectOnlyPassengerFields.class)
	@RequestMapping(value ="/{flightNumber}", method = RequestMethod.POST)
	public ResponseEntity<?> addFlight(
			@PathVariable ("flightNumber") String flightNumber,
			@RequestParam("price") int price,
			@RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureTime") String departureTime,
			@RequestParam("arrivalTime") String arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture) throws JsonProcessingException {
		
		if((Long.parseLong(arrivalTime.replace("-", "")) < Long.parseLong(departureTime.replace("-", ""))) || capacity<=0 || price <=0)
		{
			String errMsg = "The parameters given for the flight are incorrect, either the price, capacity or timings are not correct ";
			logger.debug("The parameters given for the flight are incorrect, either the price, capacity or timings are not correct");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}
		
		Flight existingFlight = flightService.getById(flightNumber);
		
		if (existingFlight == null) {
			Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
			Flight flight = new Flight();
			
			flight.setNumber(flightNumber);
			flight.setPrice(price);
			flight.setFromOrigin(from);
			flight.setToDestination(to);
			flight.setDepartureTime(departureTime);
			flight.setArrivalTime(arrivalTime);
			flight.setDescription(description);
			flight.setSeatsLeft(capacity);
			flight.setPlane(plane);

			flightService.save(flight);
			logger.debug("Created/updated flight " + flight);
			return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
	
		} else {
			ResponseEntity<?> res  = createOrUpdateFlight(existingFlight,flightNumber,departureTime,arrivalTime,capacity);
			if(res!=null)
				return res;
						
			existingFlight.setPrice(price);
			existingFlight.setFromOrigin(from);
			existingFlight.setToDestination(to);
			existingFlight.setDepartureTime(departureTime);
			existingFlight.setArrivalTime(arrivalTime);
			existingFlight.setDescription(description);
			existingFlight.getPlane().setCapacity(capacity);
			existingFlight.getPlane().setModel(model);
			existingFlight.getPlane().setManufacturer(manufacturer);
			existingFlight.getPlane().setYearOfManufacture(yearOfManufacture);
			
			flightService.save(existingFlight);
			return new ResponseEntity<Flight>(existingFlight, HttpStatus.OK);
		}

	}
	

	/**
	 * @param id
	 * @param xml
	 * @return XML or JSON GET flights response based on the request or error message
	 * @throws JsonProcessingException
	 */
	@JsonView(Views.ProjectOnlyPassengerFields.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getFlight(
			@PathVariable("id") String id,
			@RequestParam(value = "xml", required = false) boolean xml) throws JsonProcessingException {
		Flight flight = flightService.getById(id);
		if (flight == null) {
			String errMsg = "Sorry, the requested flight with id " + id + " does not exist";
			logger.debug("Sorry, the requested flight with id " + id + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
		}
		
		//Forcefully populating the passengers in the Flight table 
		List<Passenger> passengers = flight.getPassengers();
		flight.setPassengers(passengers);
		
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
			xs.omitField(Flight.class, "reservations");
			xs.omitField(Passenger.class, "reservations");
			xs.omitField(Passenger.class, "flights");
			
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
		    return new ResponseEntity<>(xs.toXML(flight),responseHeaders, HttpStatus.OK);
		}
		else
		{
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<>(flight,responseHeaders, HttpStatus.OK);
		}
	}
	
	/**
	 * @param flightNumber
	 * @param price
	 * @param from
	 * @param to
	 * @param departureTime
	 * @param arrivalTime
	 * @param description
	 * @param capacity
	 * @param model
	 * @param manufacturer
	 * @param yearOfManufacture
	 * @return JSON response of the updated flight or error message
	 * @throws JsonProcessingException
	 */
	@JsonView(Views.ProjectOnlyPassengerFields.class)
	@RequestMapping(value = "/{flightNumber}",method = RequestMethod.PUT)
	public ResponseEntity<?> updateFlight(
			@PathVariable ("flightNumber") String flightNumber,
			@RequestParam("price") int price,
			@RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureTime") String departureTime,
			@RequestParam("arrivalTime") String arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture) throws JsonProcessingException  {
		
		if((Long.parseLong(arrivalTime.replace("-", "")) < Long.parseLong(departureTime.replace("-", ""))) || capacity<=0 || price <=0)
		{
			String errMsg = "The parameters given for the flight are incorrect, either the price, capacity or timings are not correct ";
			logger.debug("The parameters given for the flight are incorrect, either the price, capacity or timings are not correct");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}
		
		Flight existingFlight = flightService.getById(flightNumber);
		
		if (existingFlight == null) {
			String errMsg = "Sorry, the requested flight with id " + flightNumber + " does not exist";
			logger.debug("Sorry, the requested flight with id " + flightNumber + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
	
		} else {
			
			ResponseEntity<?> res  = createOrUpdateFlight(existingFlight,flightNumber,departureTime,arrivalTime,capacity);
			if(res!=null)
				return res;
			
			existingFlight.setPrice(price);
			existingFlight.setFromOrigin(from);
			existingFlight.setToDestination(to);
			existingFlight.setDepartureTime(departureTime);
			existingFlight.setArrivalTime(arrivalTime);
			existingFlight.setDescription(description);
			existingFlight.getPlane().setCapacity(capacity);
			existingFlight.getPlane().setModel(model);
			existingFlight.getPlane().setManufacturer(manufacturer);
			existingFlight.getPlane().setYearOfManufacture(yearOfManufacture);
			
			flightService.save(existingFlight);
			return new ResponseEntity<Flight>(existingFlight, HttpStatus.OK);
		}
	}

	
	/**
	 * @param id
	 * @return Flight id of the deleted flight or the error response if no such flight is there
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteFlight(@PathVariable("id") String id) throws JsonProcessingException {
		Flight flight = flightService.getById(id);
		if (flight == null) {
			String errMsg = "Sorry, the requested flight with id " + id + " does not exist";
			logger.debug("Sorry, the requested flight with id " + id + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
		}else if(!flight.getReservations().isEmpty()){
			String errMsg = "Flight with " + id + " still has some reservations. It cannot be deleted";
			logger.debug("Flight with " + id + " still has some reservations. It cannot be deleted");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}
		flightService.delete(id);
		String successMsg = "Flight with number " + id + " is deleted successfully";
		logger.debug("Flight with number " + id + " is deleted successfully");
		return ControllerUtil.sendSuccess(successMsg);
	}
	
	
	
	/**
	 * @param existingFlight
	 * @param flightNumber
	 * @param departureTime
	 * @param arrivalTime
	 * @param capacity
	 * @return
	 * @throws JsonProcessingException
	 */
	ResponseEntity<?> createOrUpdateFlight(Flight existingFlight, String flightNumber,String departureTime, String arrivalTime, int capacity) throws JsonProcessingException
	{
		if(capacity < existingFlight.getPlane().getCapacity() && capacity < existingFlight.getReservations().size()){
			String errMsg = "Sorry, the capacity of the requested flight with id " + flightNumber + " cannot be less than the existing reservations";
			logger.debug("Sorry, the capacity of the requested flight with id " + flightNumber + " cannot be less than the existing reservations");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		} else {

			List<Reservation> reservations = existingFlight.getReservations();
			Set<String> flightNumberOfOtherFlights = new HashSet<String>();
			List<Long> arrivalTimes = new ArrayList<Long>();
			List<Long> departureTimes = new ArrayList<Long>();
			for(Reservation res: reservations)
			{
				arrivalTimes.clear();
				departureTimes.clear();
				flightNumberOfOtherFlights.clear();
				for(Flight resflight : res.getFlights())
				{
					if(!resflight.getNumber().equals(flightNumber))
					{
						flightNumberOfOtherFlights.add(resflight.getNumber());
					}
				}

				arrivalTimes.add(Long.parseLong(arrivalTime.replace("-", "")));
				departureTimes.add(Long.parseLong(departureTime.replace("-", "")));

				for(String flightNum : flightNumberOfOtherFlights){
					Flight otherFlight = flightService.getById(flightNum);
					departureTimes.add(Long.parseLong(otherFlight.getDepartureTime().replace("-", "")));
					arrivalTimes.add(Long.parseLong(otherFlight.getArrivalTime().replaceAll("-", "")));
				}

				if(flightService.checkIfOverlappingFlightTimes(departureTimes,arrivalTimes))
				{
					String errMsg = "There is an overlap of flight time intervals for a passenger. Flight timings cannot be modified";
					return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
				}
			}
		}
		return null;
	}

}
