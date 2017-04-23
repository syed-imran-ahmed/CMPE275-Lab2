package edu.sjsu.cmpe275.lab2.controller;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Plane;
import edu.sjsu.cmpe275.lab2.service.FlightService;

/**
 * 
 * @author Imran
 *
 */

@RestController
@RequestMapping("/flight")
public class FlightController {

	final static Logger logger = Logger.getLogger(FlightController.class);

	@Autowired
	FlightService flightService;

	@RequestMapping(value ="/{flightNumber}", method = RequestMethod.POST)
	public ResponseEntity<Flight> addFlight(
			@PathVariable ("flightNumber") String flightNumber,
			@RequestParam("price") int price,
			@RequestParam("from") String from,
			@RequestParam("to") String to,
			@RequestParam("departureTime") Long departureTime,
			@RequestParam("arrivalTime") Long arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture) {

		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		Flight flight = new Flight();
		flight.setNumber(flightNumber);
		flight.setPrice(price);
		flight.setFromOrigin(from);
		flight.setToDestination(to);
		flight.setDepartureTime(new Date(departureTime));
		flight.setArrivalTime(new Date(arrivalTime));
		flight.setDescription(description);
		flight.setSeatsLeft(capacity);
		flight.setPlane(plane);

		flightService.save(flight);
		logger.debug("Created/updated flight " + flight);
		return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Flight> getFlight(@PathVariable("id") String id) {
		Flight flight = flightService.getById(id);
		if (flight == null) {
			logger.debug("Flight with id " + id + " does not exist.");
			return new ResponseEntity<Flight>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Flight: " + flight);
		return new ResponseEntity<Flight>(flight, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteFlight(@PathVariable("id") String id) {
		Flight flight = flightService.getById(id);
		if (flight == null) {
			logger.debug("Flight with id " + id + " does not exist.");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight number "+id+ " doesn't exist");
		}
		flightService.delete(id);
		logger.debug("Deleted Flight: " + flight);
		return ResponseEntity.status(HttpStatus.OK).body("Flight number "+id+" has been deleted successfully");
	}
}
