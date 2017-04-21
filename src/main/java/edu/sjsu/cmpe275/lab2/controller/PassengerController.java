package edu.sjsu.cmpe275.lab2.controller;

import java.util.Arrays;
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

import edu.sjsu.cmpe275.lab2.model.Passenger;
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

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Passenger> addPassenger(
			@RequestParam("firstname") String firstName,
			@RequestParam("lastname") String lastName,
			@RequestParam("age") int age,
			@RequestParam("gender") String gender,
			@RequestParam("phone") String phone) {
		
		Passenger passenger = new Passenger(firstName,lastName,age,gender,phone);
		passengerService.save(passenger);
		logger.debug("Added:: " + passenger);
		return new ResponseEntity<Passenger>(passenger, HttpStatus.CREATED);
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
	public ResponseEntity<Passenger> getEmployee(@PathVariable("id") Long id) {
		Passenger passenger = passengerService.getById(id);
		if (passenger == null) {
			logger.debug("Passenger with id " + id + " does not exists");
			return new ResponseEntity<Passenger>(HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Employee:: " + passenger);
		return new ResponseEntity<Passenger>(passenger, HttpStatus.OK);
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
