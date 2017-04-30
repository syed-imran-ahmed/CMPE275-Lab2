package edu.sjsu.cmpe275.lab2.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;

import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.service.PassengerService;

public class PassengerControllerUnitTests {

	private PassengerController controller;
	private PassengerService passengerService;
	
	private static final String FIRSTNAME = "Imran";
	private static final String LASTNAME = "Ahmed";
	private static final int AGE = 29;
	private static final String GENDER = "male";
	private static final String PHONE = "123-456-7890";

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
		
        Passenger passenger = (Passenger) passengerResponseEntity.getBody();
        Assert.assertEquals(FIRSTNAME, passenger.getFirstname());
        Assert.assertEquals(LASTNAME, passenger.getLastname());
        Assert.assertEquals(AGE, passenger.getAge());
        Assert.assertEquals(GENDER, passenger.getGender());
        Assert.assertEquals(PHONE, passenger.getPhone());
	}
}
