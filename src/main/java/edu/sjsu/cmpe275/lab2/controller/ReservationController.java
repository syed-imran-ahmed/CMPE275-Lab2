package edu.sjsu.cmpe275.lab2.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Reservation;
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
		reservationService.save(reservation);
		logger.debug("Created reservation " + reservation.getOrdernumber());
		return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
	}
}
