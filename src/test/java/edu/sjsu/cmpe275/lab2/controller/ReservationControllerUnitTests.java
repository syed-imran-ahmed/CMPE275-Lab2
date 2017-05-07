package edu.sjsu.cmpe275.lab2.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
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
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;


/**
* <h1>Reservation Test Casess</h1>
* The reservation test class provides the test cases for all the
* scenarios that are provided as the requirement and has
* a code coverage of 90% or more as per cobertura
* 
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-05-03
*/ 

public class ReservationControllerUnitTests {

	ReservationController controller;
	FlightService flightService;
	ReservationService reservationService;
	PassengerService passengerService;
	
	
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
	
	private static List<String> flightNumbers = Arrays.asList(flightNumber);
	
	private static Long orderNumber = 123L;
	
	@Before
	public void setUp(){
		reservationService = PowerMockito.mock(ReservationService.class);
		passengerService = PowerMockito.mock(PassengerService.class);
		flightService = PowerMockito.mock(FlightService.class);
		controller = new ReservationController();
		controller.reservationService = reservationService;
		controller.passengerService = passengerService;
		controller.flightService = flightService;
	}
	
	//GET
	@Test
	public void test_get_reservation_nonexisting() throws JsonProcessingException{
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(null);
		ResponseEntity<?> responseEntity = controller.getReservation(orderNumber);
		HttpStatus status = responseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void test_get_reservation_existing() throws JsonProcessingException{
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
		
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(reservation);
		
		ResponseEntity<?> searchResponseEntity = controller.getReservation(orderNumber);
		
		HttpStatus status = searchResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.OK, status);
 
		Reservation actualReservation = (Reservation) searchResponseEntity.getBody();
		Assert.assertEquals(reservation.getOrdernumber(), actualReservation.getOrdernumber());
		
	}
	
	//SEARCH
	@Test
	public void test_search_reservation_all_nulls() throws JsonProcessingException{
		ResponseEntity<?> searchResponseEntity = controller.searchReservation(null, null, null, null);
		HttpStatus status = searchResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	@Test
	public void test_search_reservation() throws JsonProcessingException{
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
		
		Mockito.when(reservationService.searchReservations(PASSENGER_ID, from, to, flightNumber)).thenReturn(Collections.singletonList(reservation));
		
		ResponseEntity<?> searchResponseEntity = controller.searchReservation(PASSENGER_ID, from, to, flightNumber);
		
		HttpStatus status = searchResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.OK, status);
 
	    String reservationsInXml = (String) searchResponseEntity.getBody();
	    XStream xs = new XStream();
		xs.alias("passenger", Passenger.class);
		xs.alias("flight", Flight.class);
		xs.alias("reservation", Reservation.class);
		xs.alias("reservations", List.class);
		List<Reservation> actualReservations = (List<Reservation>) xs.fromXML(reservationsInXml);
		Assert.assertEquals(reservation.getOrdernumber(), actualReservations.get(0).getOrdernumber());
	}
	
	//DELETE
	@Test
	public void test_delete_reservation_nonexisting() throws JsonProcessingException{
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(null);
		
		ResponseEntity<?> deleteResponseEntity = controller.deleteReservation(orderNumber);
		
		HttpStatus status = deleteResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void test_delete_reservation_existing() throws JsonProcessingException{
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
		
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(reservation);
		
		ResponseEntity<?> deleteResponseEntity = controller.deleteReservation(orderNumber);
		
		HttpStatus status = deleteResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.OK, status);
		
	}
	
	//POST
	@Test
	public void test_make_reservation_nonexisting_passenger() throws JsonProcessingException{
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(null);
		
		ResponseEntity<?> makeResponseEntity = controller.makeReservation(PASSENGER_ID, flightNumbers);
		
		HttpStatus status = makeResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	@Test
	public void test_make_reservation_nonexisting_flights() throws JsonProcessingException{
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		passenger.setReservations(Collections.emptyList());
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(null);
		
		ResponseEntity<?> makeResponseEntity = controller.makeReservation(PASSENGER_ID, flightNumbers);
		
		HttpStatus status = makeResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	@Test
	public void test_make_reservation_flight_capacity_exceeded() throws JsonProcessingException{
		Passenger passenger = new Passenger(FIRSTNAME, LASTNAME, AGE, GENDER, PHONE);
		passenger.setReservations(Collections.emptyList());
		
		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		Flight flight = new Flight();
		flight.setNumber(flightNumber);
		flight.setPrice(price);
		flight.setFromOrigin(from);
		flight.setToDestination(to);
		flight.setDepartureTime(departureTime);
		flight.setArrivalTime(arrivalTime);
		flight.setDescription(description);
		flight.setSeatsLeft(0); //No seats left in this flight
		flight.setPlane(plane);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight);
		
		ResponseEntity<?> makeResponseEntity = controller.makeReservation(PASSENGER_ID, flightNumbers);
		
		HttpStatus status = makeResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);	
	}
	
	@Test
	public void test_make_reservation_conflicting_flights_inter() throws JsonProcessingException{
		Passenger passenger = new Passenger("first", "person", AGE, GENDER, PHONE);
		passenger.setReservations(Collections.emptyList());

		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		String anotherFlightNumber = "XYZ123";
		
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
		flight2.setNumber(anotherFlightNumber);
		flight2.setPrice(price);
		flight2.setFromOrigin(from);
		flight2.setToDestination(to);
		flight2.setDepartureTime("2017-01-01-01");
		flight2.setArrivalTime("2017-01-01-04");
		flight2.setDescription(description);
		flight2.setSeatsLeft(capacity);
		flight2.setPlane(plane);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight1);
		Mockito.when(flightService.getById(anotherFlightNumber)).thenReturn(flight2);
		Mockito.when(flightService.checkIfOverlappingFlightTimes(Mockito.anyList(), Mockito.anyList())).thenReturn(true);

		List<String> flightNumbers = Arrays.asList(flightNumber, anotherFlightNumber);
		ResponseEntity<?> makeResponseEntity = controller.makeReservation(PASSENGER_ID, flightNumbers);
		
		HttpStatus status = makeResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	@Test
	public void test_make_reservation_conflicting_flights_intra() throws JsonProcessingException{
		Passenger passenger = new Passenger("first", "person", AGE, GENDER, PHONE);
		passenger.setReservations(Collections.emptyList());

		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		String anotherFlightNumber = "XYZ123";
		
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
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		reservation.setPrice(flight1.getPrice());
		reservation.setFlights(Collections.singletonList(flight1));
		
		passenger.setReservations(Collections.singletonList(reservation));
		flight1.setReservations(Collections.singletonList(reservation));
		
		Flight flight2 = new Flight();
		flight2.setNumber(anotherFlightNumber);
		flight2.setPrice(price);
		flight2.setFromOrigin(from);
		flight2.setToDestination(to);
		flight2.setDepartureTime("2017-01-01-01");
		flight2.setArrivalTime("2017-01-01-04");
		flight2.setDescription(description);
		flight2.setSeatsLeft(capacity);
		flight2.setPlane(plane);
		
		Mockito.when(passengerService.getById(PASSENGER_ID)).thenReturn(passenger);
		Mockito.when(flightService.getById(flightNumber)).thenReturn(flight1);
		Mockito.when(flightService.getById(anotherFlightNumber)).thenReturn(flight2);
		Mockito.when(flightService.checkIfOverlappingFlightTimes(Mockito.anyList(), Mockito.anyList())).thenReturn(true);

		List<String> flightNumbers = Arrays.asList(anotherFlightNumber);
		ResponseEntity<?> makeResponseEntity = controller.makeReservation(PASSENGER_ID, flightNumbers);
		
		HttpStatus status = makeResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
	
	//UPDATE
	@Test
	public void test_update_reservation_nonexisting() throws JsonProcessingException{
		List<String> flightsAdded = Arrays.asList("XYZ123");
		List<String> flightsRemoved = Arrays.asList("ABC123");
		
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(null);
		
		ResponseEntity<?> updateResponseEntity = controller.updateReservation(orderNumber, flightsAdded, flightsRemoved);
		
		HttpStatus status = updateResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void test_update_reservation_empty_flights_list() throws JsonProcessingException{
		Passenger passenger = new Passenger("first", "person", AGE, GENDER, PHONE);

		Plane plane = new Plane(capacity, model, manufacturer, yearOfManufacture);
		
		Flight flight = new Flight();
		flight.setNumber(flightNumber);
		flight.setPrice(price);
		flight.setFromOrigin(from);
		flight.setToDestination(to);
		flight.setDepartureTime("2017-01-01-00");
		flight.setArrivalTime("2017-01-01-02");
		flight.setDescription(description);
		flight.setSeatsLeft(capacity);
		flight.setPlane(plane);
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		reservation.setPrice(flight.getPrice());
		reservation.setFlights(Collections.singletonList(flight));
		
		List<String> flightsAdded = Collections.emptyList();
		List<String> flightsRemoved = Arrays.asList(flightNumber);
		
		Mockito.when(reservationService.getById(orderNumber)).thenReturn(reservation);
		
		ResponseEntity<?> updateResponseEntity = controller.updateReservation(orderNumber, flightsAdded, flightsRemoved);
		
		HttpStatus status = updateResponseEntity.getStatusCode();
		Assert.assertEquals(HttpStatus.BAD_REQUEST, status);
	}
}
