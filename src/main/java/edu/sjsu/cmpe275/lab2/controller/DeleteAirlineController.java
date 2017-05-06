package edu.sjsu.cmpe275.lab2.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.service.FlightService;


/**
* <h1>Airline Delete Endpoints</h1>
* The airline controller class provides the DELETE request for
* its corresponding operation as requested with a separate API 
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-25
*/ 

@RestController
@RequestMapping("/airline")
public class DeleteAirlineController {

	final static Logger logger = Logger.getLogger(FlightController.class);
	
	@Autowired
	FlightService flightService;
	
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
	
}
