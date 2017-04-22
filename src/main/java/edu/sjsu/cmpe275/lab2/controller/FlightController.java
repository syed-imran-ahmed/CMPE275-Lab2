package edu.sjsu.cmpe275.lab2.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.sjsu.cmpe275.lab2.model.Flight;
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
			@RequestParam("departureTime") Date departureTime,
			@RequestParam("arrivalTime") Date arrivalTime,
			@RequestParam("description") String description,
			@RequestParam("capacity") int capacity,
			@RequestParam("model") String model,
			@RequestParam("manufacturer") String manufacturer,
			@RequestParam("yearOfManufacture") int yearOfManufacture) {
		
		
		
		Flight flight = new Flight(flightNumber,price,from,to,departureTime,arrivalTime,capacity,description);
		flightService.save(flight);
		logger.debug("Added:: " + flight);
		return new ResponseEntity<Flight>(flight, HttpStatus.CREATED);
	}


//	@RequestMapping(method = RequestMethod.PUT)
//	public ResponseEntity<Void> updateEmployee(@RequestBody Employee employee) {
//		Employee existingEmp = empService.getById(employee.getId());
//		if (existingEmp == null) {
//			logger.debug("Employee with id " + employee.getId() + " does not exists");
//			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//		} else {
//			empService.save(employee);
//			return new ResponseEntity<Void>(HttpStatus.OK);
//		}
//	}


	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Flight> getEmployee(@PathVariable("id") Long id) {
		Flight flight = flightService.getById(id);
		if (flight == null) {
			logger.debug("Passenger with id " + id + " does not exists");
			return new ResponseEntity<Flight>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Employee:: " + flight);
		return new ResponseEntity<Flight>(flight, HttpStatus.OK);
	}


//	@RequestMapping(method = RequestMethod.GET)
//	public ResponseEntity<List<Employee>> getAllEmployees() {
//		List<Employee> employees = empService.getAll();
//		if (employees.isEmpty()) {
//			logger.debug("Employees does not exists");
//			return new ResponseEntity<List<Employee>>(HttpStatus.NO_CONTENT);
//		}
//		logger.debug("Found " + employees.size() + " Employees");
//		logger.debug(Arrays.toString(employees.toArray()));
//		return new ResponseEntity<List<Employee>>(employees, HttpStatus.OK);
//	}


//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
//		Employee employee = empService.getById(id);
//		if (employee == null) {
//			logger.debug("Employee with id " + id + " does not exists");
//			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
//		} else {
//			empService.delete(id);
//			logger.debug("Employee with id " + id + " deleted");
//			return new ResponseEntity<Void>(HttpStatus.GONE);
//		}
//	}

}
