package edu.sjsu.cmpe275.lab2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
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
	public ResponseEntity<Reservation> makeReservation(
			@RequestParam("passengerId") Long passengerId,
			@RequestParam("flightList") List<String> flightNumbers
			) {

		Passenger passenger = passengerService.getById(passengerId);
		List<Flight> flights = new ArrayList<Flight>();
		for(String flightNumber : flightNumbers){
			flights.add(flightService.getById(flightNumber));
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
		return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
	}
	
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Reservation> getReservation(@PathVariable("id") Long id) {
		Reservation reservation = reservationService.getById(id);
		if (reservation == null) {
			logger.debug("Flight with id " + id + " does not exist.");
			return new ResponseEntity<Reservation>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Flight: " + reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteReservation(@PathVariable("id") Long id) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(id);
		if (reservation == null) {
			String errMsg = "Reservation with number " + id + " does not exists";
			logger.debug("Reservation with number " + id + " does not exists");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();
			JsonNode childNodes = mapper.createObjectNode();
			((ObjectNode) childNodes).put("code", HttpStatus.NOT_FOUND.toString());
			((ObjectNode) childNodes).put("msg", errMsg);
					
			((ObjectNode) rootNode).set("BadRequest", childNodes);
			String jsonString = mapper.writeValueAsString(rootNode);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<String>(jsonString,responseHeaders,HttpStatus.NOT_FOUND);
			
		} else {
			List<Flight> flights1 = reservation.getFlights();
			for(Flight flight : flights1){
				int seats = flight.getSeatsLeft();
				flight.setSeatsLeft(seats+1);
			}
			
			reservationService.delete(id);
			logger.debug("Reservation with number " + id + " deleted");
			return new ResponseEntity<Passenger>(HttpStatus.OK);
		}
	}
}
