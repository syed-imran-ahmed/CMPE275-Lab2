package edu.sjsu.cmpe275.lab2.controller;

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
import edu.sjsu.cmpe275.lab2.service.ReservationService;

public class ReservationControllerUnitTests {

	ReservationController controller;
	ReservationService service;
	
	
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
	
	private static Long orderNumber = 123L;
	
	@Before
	public void setUp(){
		service = PowerMockito.mock(ReservationService.class);
		controller = new ReservationController();
		controller.reservationService = service;
	}
	
	//GET
	@Test
	public void test_get_reservation_nonexisting() throws JsonProcessingException{
		Mockito.when(service.getById(orderNumber)).thenReturn(null);
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
		
		Mockito.when(service.getById(orderNumber)).thenReturn(reservation);
		
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
		
		Mockito.when(service.searchReservations(PASSENGER_ID, from, to, flightNumber)).thenReturn(Collections.singletonList(reservation));
		
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
	public void test_delete_reservation_nonexisting(){
		
	}
	
	@Test
	public void test_delete_reservation_existing(){
		
	}
	
	//POST
	@Test
	public void test_make_reservation_nonexisting_passenger(){
		
	}
	
	@Test
	public void test_make_reservation_nonexisting_flights(){
		
	}
	
	@Test
	public void test_make_reservation_flight_capacity_exceeded(){
		
	}
	
	@Test
	public void test_make_reservation_conflicting_flights_inter(){
		
	}
	
	@Test
	public void test_make_reservation_conflicting_flights_intra(){
		
	}
	
	//UPDATE
	@Test
	public void test_update_reservation_nonexisting(){
		
	}
	
	@Test
	public void test_update_reservation_empty_flights_list(){
		
	}
	
	@Test
	public void test_update_reservation_with_conflicting_flights(){
		
	}
	
	@Test
	public void test_update_reservations_no_conflict(){
		
	}
}
