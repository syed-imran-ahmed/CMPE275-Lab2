package edu.sjsu.cmpe275.lab2.controller;

import java.util.List;

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
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;

@RestController
@RequestMapping("/reservation")

public class ReservationController {

	final static Logger logger = Logger.getLogger(ReservationController.class);
	
	@Autowired
	ReservationService reservationService;
	
	@Autowired
	PassengerService passengerService;
	
	@Autowired
	FlightService flightService;
	
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Reservation> addReservation(
			@RequestParam("passengerId") Long id,
			@RequestParam("flightLists") List<Flight> flights) {
		
		Passenger passenger = passengerService.getById(id);
		int price = 0;
		for (int i = 0; i< flights.size(); i++)
		{
			      Flight flight = flightService.getById(flights.get(i));
			      price = price+ flight.price
			      
		}
		
		
		Reservation reservation = new Reservation(price, flights, passenger);
		reservationService.save(reservation);
		logger.debug("Added:: " + reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.CREATED);
	}
	

       @RequestMapping(value = "/{number}", method = RequestMethod.PUT)
        public ResponseEntity<Reservation> updateReservation(
        		@PathVariable("number") String number,
        		@RequestParam("flightsAdded") List<Flight> flights1,
        		@RequestParam("flightsRemoved") List<Flight> flights2) {
    	   
    	   
    	   for (int i = 0; i< flights2.size(); i++)
   		{
   			      flightService.delete(flights2.get(i));
   			      
   			      
   		}
    	   
     
    	   Reservation reservation = reservationService.getById(number); 
       return new ResponseEntity<Reservation>(reservation, HttpStatus.OK)
       }
	     



	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Passenger> getEmployee(@PathVariable("id") Long id) {
		Passenger passenger = passengerService.getById(id);
		if (passenger == null) {
			logger.debug("Passenger with id " + id + " does not exists");
			return new ResponseEntity<Passenger>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Employee:: " + passenger);
		return new ResponseEntity<Passenger>(passenger, HttpStatus.OK);
	}


	@RequestMapping(method = RequestMethod.GET)
//	public ResponseEntity<Reservation> getReservation() {
//		List<Employee> employees = empService.getAll();
//		if (employees.isEmpty()) {
//			logger.debug("Employees does not exists");
//			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
//		}
//		logger.debug("Found " + employees.size() + " Employees");
//		logger.debug(Arrays.toString(employees.toArray()));
//		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
//	}


	@RequestMapping(value = "/{number}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteReservation(@PathVariable("number") Long number) {
		Reservation reservation = reservationService.getById(number);
		if (reservation == null) {
			logger.debug("Reservation with order number " + number + " does not exists");
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		} else {
			reservationService.delete(number);
			logger.debug("Employee with id " + number + " deleted");
			return new ResponseEntity<Void>(HttpStatus.GONE);
		}
	}

	}

