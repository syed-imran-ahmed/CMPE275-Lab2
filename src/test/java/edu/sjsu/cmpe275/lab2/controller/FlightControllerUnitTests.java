package edu.sjsu.cmpe275.lab2.controller;

import java.util.Arrays;
import java.util.Collections;

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
import edu.sjsu.cmpe275.lab2.service.FlightService;

/**
* <h1>Flight Test Casess</h1>
* The flight test class provides the test cases for all the
* scenarios that are provided as the requirement and has
* a code coverage of 90% or more as per cobertura
* 
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-05-01
*/ 

public class FlightControllerUnitTests {

	private FlightController controller;
	private FlightService flightService;

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
		flightService = PowerMockito.mock(FlightService.class);
		controller = new FlightController();
		controller.flightService = flightService;
	}
	
	@Test
	public void test_add_flight_non_existing_all_valid() throws JsonProcessingException{
		
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
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		Mockito.when(flightService.save(flight)).thenReturn(flight);
        
        ResponseEntity<?> flightResponseEntity = controller.addFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.CREATED, statusCode);
        
        Flight actualFlight = (Flight) flightResponseEntity.getBody();
        Assert.assertEquals(flight.getNumber(), actualFlight.getNumber());
        Assert.assertEquals(flight.getPrice(), actualFlight.getPrice());
        Assert.assertEquals(flight.getFromOrigin(), actualFlight.getFromOrigin());
        Assert.assertEquals(flight.getToDestination(), actualFlight.getToDestination());
        Assert.assertEquals(flight.getDepartureTime(), actualFlight.getDepartureTime());
        Assert.assertEquals(flight.getArrivalTime(), actualFlight.getArrivalTime());
        Assert.assertEquals(flight.getDescription(), actualFlight.getDescription());
        Assert.assertEquals(flight.getPlane().getCapacity(), actualFlight.getPlane().getCapacity());
        Assert.assertEquals(flight.getPlane().getModel(), actualFlight.getPlane().getModel());
        Assert.assertEquals(flight.getPlane().getManufacturer(), actualFlight.getPlane().getManufacturer());
        Assert.assertEquals(flight.getPlane().getYearOfManufacture(), actualFlight.getPlane().getYearOfManufacture());
	}
	
	@Test
	public void test_add_flight_non_existing_invalid_arrival_departure_time() throws JsonProcessingException{
       
		String departureTime = "2017-09-03-19";
		String arrivalTime = "2017-09-03-18";//setting arrivalTime before departureTime to make this input invalid.
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
        
        ResponseEntity<?> flightResponseEntity = controller.addFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}

	@Test
	public void test_add_flight_existing_all_valid() throws JsonProcessingException{
		
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
		flight.setReservations(Collections.emptyList());
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
        
        ResponseEntity<?> flightResponseEntity = controller.addFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        Flight actualFlight = (Flight) flightResponseEntity.getBody();
        Assert.assertEquals(flight.getNumber(), actualFlight.getNumber());
        Assert.assertEquals(flight.getPrice(), actualFlight.getPrice());
        Assert.assertEquals(flight.getFromOrigin(), actualFlight.getFromOrigin());
        Assert.assertEquals(flight.getToDestination(), actualFlight.getToDestination());
        Assert.assertEquals(flight.getDepartureTime(), actualFlight.getDepartureTime());
        Assert.assertEquals(flight.getArrivalTime(), actualFlight.getArrivalTime());
        Assert.assertEquals(flight.getDescription(), actualFlight.getDescription());
        Assert.assertEquals(flight.getPlane().getCapacity(), actualFlight.getPlane().getCapacity());
        Assert.assertEquals(flight.getPlane().getModel(), actualFlight.getPlane().getModel());
        Assert.assertEquals(flight.getPlane().getManufacturer(), actualFlight.getPlane().getManufacturer());
        Assert.assertEquals(flight.getPlane().getYearOfManufacture(), actualFlight.getPlane().getYearOfManufacture());
	}

	@Test
	public void test_add_flight_existing_invalid_arrival_departure_time() throws JsonProcessingException{
       
		String departureTime = "2017-09-03-19";
		String arrivalTime = "2017-09-03-18";//setting arrivalTime before departureTime to make this input invalid.
		
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
		
		Mockito.when(flightService.save(flight)).thenReturn(flight);
        
        ResponseEntity<?> flightResponseEntity = controller.addFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}

	@Test
	public void test_add_flight_existing_calls_update_internally() throws JsonProcessingException{
		test_update_flight_with_less_capacity_than_reservations();
		test_update_flight_with_overlapping_time_inter_reservation();
		test_update_flight_with_overlapping_time_intra_reservation();
	}

	@Test
	public void test_get_flight_existing_xml_true() throws JsonProcessingException{
		
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
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		ResponseEntity<?> flightResponseEntity = controller.getFlight(flightNumber, true);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        String actualFlightInXml = (String) flightResponseEntity.getBody();
        XStream xs = new XStream();
        xs.alias("flight", Flight.class);
        Flight actualFlight = (Flight) xs.fromXML(actualFlightInXml);
        Assert.assertEquals(flight.getNumber(), actualFlight.getNumber());
        Assert.assertEquals(flight.getPrice(), actualFlight.getPrice());
        Assert.assertEquals(flight.getFromOrigin(), actualFlight.getFromOrigin());
        Assert.assertEquals(flight.getToDestination(), actualFlight.getToDestination());
        Assert.assertEquals(flight.getDepartureTime(), actualFlight.getDepartureTime());
        Assert.assertEquals(flight.getArrivalTime(), actualFlight.getArrivalTime());
        Assert.assertEquals(flight.getDescription(), actualFlight.getDescription());
        Assert.assertEquals(flight.getPlane().getCapacity(), actualFlight.getPlane().getCapacity());
        Assert.assertEquals(flight.getPlane().getModel(), actualFlight.getPlane().getModel());
        Assert.assertEquals(flight.getPlane().getManufacturer(), actualFlight.getPlane().getManufacturer());
        Assert.assertEquals(flight.getPlane().getYearOfManufacture(), actualFlight.getPlane().getYearOfManufacture());
	}
	
	@Test
	public void test_get_flight_existing_xml_false() throws JsonProcessingException{
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
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		ResponseEntity<?> flightResponseEntity = controller.getFlight(flightNumber, false);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
        
        Flight actualFlight = (Flight) flightResponseEntity.getBody();
        Assert.assertEquals(flight.getNumber(), actualFlight.getNumber());
        Assert.assertEquals(flight.getPrice(), actualFlight.getPrice());
        Assert.assertEquals(flight.getFromOrigin(), actualFlight.getFromOrigin());
        Assert.assertEquals(flight.getToDestination(), actualFlight.getToDestination());
        Assert.assertEquals(flight.getDepartureTime(), actualFlight.getDepartureTime());
        Assert.assertEquals(flight.getArrivalTime(), actualFlight.getArrivalTime());
        Assert.assertEquals(flight.getDescription(), actualFlight.getDescription());
        Assert.assertEquals(flight.getPlane().getCapacity(), actualFlight.getPlane().getCapacity());
        Assert.assertEquals(flight.getPlane().getModel(), actualFlight.getPlane().getModel());
        Assert.assertEquals(flight.getPlane().getManufacturer(), actualFlight.getPlane().getManufacturer());
        Assert.assertEquals(flight.getPlane().getYearOfManufacture(), actualFlight.getPlane().getYearOfManufacture());
	}
	
	@Ignore //Because for bad request, response is always in JSON.
	@Test
	public void test_get_flight_non_existing_xml_true() throws JsonProcessingException{
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		ResponseEntity<?> flightResponseEntity = controller.getFlight(flightNumber, true);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_get_flight_non_existing_xml_false() throws JsonProcessingException{
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		ResponseEntity<?> flightResponseEntity = controller.getFlight(flightNumber, false);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_delete_flight_non_existing() throws JsonProcessingException{
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		ResponseEntity<?> response = controller.deleteFlight(flightNumber);
		
		HttpStatus statusCode = response.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_delete_flight_with_reservations() throws JsonProcessingException{
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
		flight.setReservations(Collections.singletonList(reservation));
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		ResponseEntity<?> flightResponseEntity = controller.deleteFlight(flightNumber);
		
		HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}
	
	@Test
	public void test_delete_flight_without_reservations() throws JsonProcessingException{
		
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
		flight.setReservations(Collections.emptyList());
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		Mockito.doNothing().when(flightService).delete(flightNumber);
		
		ResponseEntity<?> flightResponseEntity = controller.deleteFlight(flightNumber);
		
		HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.OK, statusCode);
	}
	
	@Test
	public void test_update_flight_non_existing() throws JsonProcessingException{
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		ResponseEntity<?> flightResponseEntity = controller.updateFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.NOT_FOUND, statusCode);
	}
	
	@Test
	public void test_update_flight_with_less_capacity_than_reservations() throws JsonProcessingException{

		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		
		Plane plane = new Plane(1, model, manufacturer, yearOfManufacture);
		
		Flight flight = new Flight();
		flight.setNumber(flightNumber);
		flight.setPrice(price);
		flight.setFromOrigin(from);
		flight.setToDestination(to);
		flight.setDepartureTime(departureTime);
		flight.setArrivalTime(arrivalTime);
		flight.setDescription(description);
		flight.setSeatsLeft(0); //because we are about to add one reservation in this flight
		flight.setPlane(plane);
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		reservation.setPrice(flight.getPrice());
		reservation.setFlights(Collections.singletonList(flight));
		
		passenger.setReservations(Collections.singletonList(reservation));
		flight.setReservations(Collections.singletonList(reservation));
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		ResponseEntity<?> flightResponseEntity = controller.updateFlight(flightNumber,
        		price,
        		from,
        		to,
        		departureTime,
        		arrivalTime,
        		description,
        		0,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);
	}
	
	@Test
	public void test_update_flight_with_overlapping_time_inter_reservation() throws JsonProcessingException{
		Passenger passenger = new Passenger("first", "person", AGE, GENDER, PHONE);
		
		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		Flight flight1 = new Flight();
		flight1.setNumber(flightNumber);
		flight1.setPrice(price);
		flight1.setFromOrigin(from);
		flight1.setToDestination(to);
		flight1.setDepartureTime("2017-01-01-00");
		flight1.setArrivalTime("2017-01-01-02");
		flight1.setDescription(description);
		flight1.setSeatsLeft(capacity);
		flight1.setPlane(plane);
		
		Flight flight2 = new Flight();
		flight2.setNumber("XYZ123");
		flight2.setPrice(price);
		flight2.setFromOrigin(from);
		flight2.setToDestination(to);
		flight2.setDepartureTime("2017-01-01-03");
		flight2.setArrivalTime("2017-01-03-05");
		flight2.setDescription(description);
		flight2.setSeatsLeft(capacity);
		flight2.setPlane(plane);
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		reservation.setPrice(flight1.getPrice());
		reservation.setFlights(Arrays.asList(flight1, flight2));
		
		passenger.setReservations(Collections.singletonList(reservation));
		flight1.setReservations(Collections.singletonList(reservation));
		flight2.setReservations(Collections.singletonList(reservation));

		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight1);
		Mockito.when(flightService.getById("XYZ123")).thenReturn(flight2);
		Mockito.when(flightService.checkIfOverlappingFlightTimes(Mockito.anyList(), Mockito.anyList())).thenReturn(true);
		
		ResponseEntity<?> flightResponseEntity = controller.updateFlight(flightNumber,
        		price,
        		from,
        		to,
        		"2017-01-01-04",
        		"2017-01-01-06",
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);	
	}
	
	@Test
	public void test_update_flight_with_overlapping_time_intra_reservation() throws JsonProcessingException{
		Passenger passenger = new Passenger("first", "person", AGE, GENDER, PHONE);
		
		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		Flight flight1 = new Flight();
		flight1.setNumber(flightNumber);
		flight1.setPrice(price);
		flight1.setFromOrigin(from);
		flight1.setToDestination(to);
		flight1.setDepartureTime("2017-01-01-00");
		flight1.setArrivalTime("2017-01-01-02");
		flight1.setDescription(description);
		flight1.setSeatsLeft(capacity);
		flight1.setPlane(plane);
		
		Flight flight2 = new Flight();
		flight2.setNumber("XYZ123");
		flight2.setPrice(price);
		flight2.setFromOrigin(from);
		flight2.setToDestination(to);
		flight2.setDepartureTime("2017-01-01-03");
		flight2.setArrivalTime("2017-01-03-05");
		flight2.setDescription(description);
		flight2.setSeatsLeft(capacity);
		flight2.setPlane(plane);
		
		Reservation reservation1 = new Reservation();
		reservation1.setPassenger(passenger);
		reservation1.setPrice(flight1.getPrice());
		reservation1.setFlights(Arrays.asList(flight1));
		
		Reservation reservation2 = new Reservation();
		reservation2.setPassenger(passenger);
		reservation2.setPrice(flight1.getPrice());
		reservation2.setFlights(Arrays.asList(flight1));
		
		passenger.setReservations(Arrays.asList(reservation1, reservation2));
		flight1.setReservations(Collections.singletonList(reservation1));
		flight2.setReservations(Collections.singletonList(reservation2));

		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight1);
		Mockito.when(flightService.getById("XYZ123")).thenReturn(flight2);
		Mockito.when(flightService.checkIfOverlappingFlightTimes(Mockito.anyList(), Mockito.anyList())).thenReturn(true);
		
		ResponseEntity<?> flightResponseEntity = controller.updateFlight(flightNumber,
        		price,
        		from,
        		to,
        		"2017-01-01-04",
        		"2017-01-01-06",
        		description,
        		capacity,
        		model,
        		manufacturer,
        		yearOfManufacture);
		
        HttpStatus statusCode = flightResponseEntity.getStatusCode();
        Assert.assertEquals(HttpStatus.BAD_REQUEST, statusCode);	
	}
}