package edu.sjsu.cmpe275.lab2.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.xstream.XStream;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Plane;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.service.PassengerService;

/**
* <h1>Passenger Test Casess</h1>
* The passenger test class provides the test cases for all the
* scenarios that are provided as the requirement and has
* a code coverage of 90% or more as per cobertura
* 
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-05-02
*/ 

public class PassengerControllerUnitTests {

	private PassengerController controller;
	private PassengerService passengerService;
	
	private static final Long PASSENGER_ID = 1L;
	private static final String FIRSTNAME = "Imran";
	private static final String LASTNAME = "Ahmed";
	private static final int AGE = 29;
	private static final String GENDER = "male";
	private static final String PHONE = "123-456-7890";
	
	private static int capacity = 2; 
	private static String model = "Boeing 747";
	private static String manufacturer = "Boeing Inc.";
	private static int yearOfManufacture = 2015;

	private static String flightNumber = "AB123";
	private static int price = 100;
	private static String from = "SEA";
	private static String to = "SJC";
	private static String departureTime = "2017-09-03-19";
	private static String arrivalTime = "2017-09-03-22";
	private static String description = "My flight";
	

	@Before
	public void setUp(){
		passengerService = PowerMockito.mock(PassengerService.class);
		controller = new PassengerController();
		controller.passengerService = passengerService;
	}

	@Test
	public void test_add_passenger_when_no_existing_passenger_by_phone() throws JsonProcessingException{
		List<Passenger> emptyPassengers = new ArrayList<Passenger>();
       
		Mockito.when(passengerService.getByPhone(PHONE)).thenReturn(emptyPassengers);
        
        ResponseEntity<?> passengerResponseEntity = controller.addPassenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.CREATED, statusCode);
        
        Passenger passenger = (Passenger) passengerResponseEntity.getBody();
        Assert.assertEquals(FIRSTNAME, passenger.getFirstname());
        Assert.assertEquals(LASTNAME, passenger.getLastname());
        Assert.assertEquals(AGE, passenger.getAge());
        Assert.assertEquals(GENDER, passenger.getGender());
        Assert.assertEquals(PHONE, passenger.getPhone());
	}
	
	@Test
	public void test_add_passenger_when_existing_passenger_by_phone() throws JsonProcessingException{
		Passenger passenger = new Passenger("somebody", "else", 23, "female", PHONE);
		List<Passenger> existingPassenger = new ArrayList<Passenger>();
		existingPassenger.add(passenger);
        
		Mockito.when(passengerService.getByPhone(PHONE)).thenReturn(existingPassenger);
        
        ResponseEntity<?> passengerResponseEntity = controller.addPassenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}
	
	@Ignore //Because for bad request, response is always in JSON.
	@Test
	public void test_get_passenger_non_existing_xml_true() throws JsonProcessingException{
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(null);
		
		ResponseEntity<?> passengerResponseEntity = controller.getPassenger(PASSENGER_ID, true);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_get_passenger_existing_xml_true() throws JsonProcessingException{
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		ResponseEntity<?> passengerResponseEntity = controller.getPassenger(PASSENGER_ID, true);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        String actualPassengerInXml = (String) passengerResponseEntity.getBody();
        XStream xs = new XStream();
        xs.alias("passenger", Passenger.class);
        Passenger actualPassenger = (Passenger) xs.fromXML(actualPassengerInXml);
        Assert.assertEquals(passenger.getFirstname(), actualPassenger.getFirstname());
        Assert.assertEquals(passenger.getLastname(), actualPassenger.getLastname());
        Assert.assertEquals(passenger.getAge(), actualPassenger.getAge());
        Assert.assertEquals(passenger.getGender(), actualPassenger.getGender());
        Assert.assertEquals(passenger.getPhone(), actualPassenger.getPhone());
	}
	
	@Test
	public void test_get_passenger_non_existing_xml_false() throws JsonProcessingException{
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(null);
		
		ResponseEntity<?> passengerResponseEntity = controller.getPassenger(PASSENGER_ID, false);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_get_passenger_existing_xml_false() throws JsonProcessingException{
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		ResponseEntity<?> passengerResponseEntity = controller.getPassenger(PASSENGER_ID, false);
		
        HttpStatus statusCode = passengerResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        Passenger actualPassenger = (Passenger) passengerResponseEntity.getBody();
        Assert.assertEquals(passenger.getFirstname(), actualPassenger.getFirstname());
        Assert.assertEquals(passenger.getLastname(), actualPassenger.getLastname());
        Assert.assertEquals(passenger.getAge(), actualPassenger.getAge());
        Assert.assertEquals(passenger.getGender(), actualPassenger.getGender());
        Assert.assertEquals(passenger.getPhone(), actualPassenger.getPhone());
	}
	
	@Test
	public void test_delete_passenger_non_existing() throws JsonProcessingException{
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(null);
		
		ResponseEntity<?> response = controller.deletePassenger(PASSENGER_ID);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_delete_passenger_existing() throws JsonProcessingException{
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		Flight flight = new Flight();
		flight.setNumber(flightNumber);
		flight.setPrice(price);
		flight.setFromOrigin(from);
		flight.setToDestination(to);
		flight.setDepartureTime(departureTime);
		flight.setArrivalTime(arrivalTime);
		flight.setDescription(description);
		flight.setSeatsLeft(capacity-1);
		flight.setPlane(plane);
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		reservation.setPrice(flight.getPrice());
		reservation.setFlights(Collections.singletonList(flight));
		
		passenger.setReservations(Collections.singletonList(reservation));
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		Mockito.doNothing().when(passengerService).delete(PASSENGER_ID);
		
		ResponseEntity<?> response = controller.deletePassenger(PASSENGER_ID);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        String responseXml = (String) response.getBody();
        XStream xs = new XStream();
        xs.alias("Response", Response.class);
        Response responseMsg = (Response) xs.fromXML(responseXml);

        Assert.assertEquals(responseMsg.getCode(), HttpStatus.OK.toString());

        List<Reservation> reservations = passenger.getReservations();
		for(Reservation modifiedReservation : reservations){
			for(Flight modifiedFlight : modifiedReservation.getFlights()){
				Assert.assertEquals(capacity, modifiedFlight.getSeatsLeft());
			}
		}
	}
	
	@Test
	public void test_put_passenger_non_existing() throws JsonProcessingException{
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(null);
		
		ResponseEntity<?> response = controller.updatePassenger(PASSENGER_ID, FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
		
	}
	
	@Test
	public void test_put_existing_passenger() throws JsonProcessingException{
		
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		Mockito.when(passengerService.getById(PHONE)).thenReturn(passenger);
		
		ResponseEntity<?> response = controller.updatePassenger(PASSENGER_ID, FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
		
	}
	
	@Test
	public void test_put_existing_passenger_with_same_phone() throws JsonProcessingException{
		
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Passenger anotherPassenger = new Passenger("somebody", "else", 38, "female", PHONE);
		anotherPassenger.setId(PASSENGER_ID+1); //make sure it is really a different passenger.
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		Mockito.when(passengerService.getByPhone(PHONE)).thenReturn(Collections.singletonList(anotherPassenger));
		
		ResponseEntity<?> response = controller.updatePassenger(PASSENGER_ID, FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}
}