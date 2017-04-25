package edu.sjsu.cmpe275.lab2.controller;

import org.apache.log4j.Logger;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.thoughtworks.xstream.XStream;

import edu.sjsu.cmpe275.lab2.model.Passenger;
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
			try{
				passengerService.save(passenger);
				logger.debug("Added:: " + passenger);
				return new ResponseEntity<Passenger>(passenger, HttpStatus.CREATED);
			}
			catch(DataAccessException e)
			{
				ObjectMapper mapper = new ObjectMapper();
				JsonNode rootNode = mapper.createObjectNode();
				JsonNode childNodes = mapper.createObjectNode();
				((ObjectNode) childNodes).put("code", HttpStatus.BAD_REQUEST.toString());
				((ObjectNode) childNodes).put("msg", e.getLocalizedMessage());
						
				((ObjectNode) rootNode).set("BadRequest", childNodes);
				String jsonString = mapper.writeValueAsString(rootNode);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
						
				return new ResponseEntity<String>(jsonString,responseHeaders,HttpStatus.BAD_REQUEST);
			}
		}
		else
		{
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();
			JsonNode childNodes = mapper.createObjectNode();
			((ObjectNode) childNodes).put("code", HttpStatus.BAD_REQUEST.toString());
			((ObjectNode) childNodes).put("msg", "another passenger with the same phone number already exists.");
					
			((ObjectNode) rootNode).set("BadRequest", childNodes);
			String jsonString = mapper.writeValueAsString(rootNode);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
					
			return new ResponseEntity<String>(jsonString,responseHeaders,HttpStatus.BAD_REQUEST);
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
			String errMsg = "Employee with id " + id + " does not exists";
			logger.debug("Employee with id " + id + " does not exists");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();
			JsonNode childNodes = mapper.createObjectNode();
			((ObjectNode) childNodes).put("code", HttpStatus.BAD_REQUEST.toString());
			((ObjectNode) childNodes).put("msg", errMsg);
					
			((ObjectNode) rootNode).set("BadRequest", childNodes);
			String jsonString = mapper.writeValueAsString(rootNode);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
					
			return new ResponseEntity<String>(jsonString,responseHeaders,HttpStatus.NOT_FOUND);
	
		} else {
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
	@RequestMapping(value = "/{id}", method = RequestMethod.GET,produces={MediaType.APPLICATION_JSON_VALUE, 
            MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<?> getPassengerInXML(
			@PathVariable("id") Long id,
			@RequestParam(value = "xml", required = false) boolean xml,
			@RequestParam(value = "json", required = false) boolean json) {
		Passenger passenger = passengerService.getById(id);
		if (passenger == null) {
			logger.debug("Passenger with id " + id + " does not exists");
			return new ResponseEntity<Passenger>(HttpStatus.NOT_FOUND);
		}
		XStream xs = new XStream();
		HttpHeaders responseHeaders;
		if(xml)
		{
			responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
		    return new ResponseEntity<String>(xs.toXML(passenger),responseHeaders, HttpStatus.OK);
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
		Passenger employee = passengerService.getById(id);
		if (employee == null) {
			String errMsg = "Employee with id " + id + " does not exists";
			logger.debug("Employee with id " + id + " does not exists");
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.createObjectNode();
			JsonNode childNodes = mapper.createObjectNode();
			((ObjectNode) childNodes).put("code", HttpStatus.BAD_REQUEST.toString());
			((ObjectNode) childNodes).put("msg", errMsg);
					
			((ObjectNode) rootNode).set("BadRequest", childNodes);
			String jsonString = mapper.writeValueAsString(rootNode);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
		    return new ResponseEntity<String>(jsonString,responseHeaders,HttpStatus.NOT_FOUND);
			
		} else {
			passengerService.delete(id);
			logger.debug("Passenger with id " + id + " deleted");
			return new ResponseEntity<Passenger>(HttpStatus.GONE);
		}
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


	

}
