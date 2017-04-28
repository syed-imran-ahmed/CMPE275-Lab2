package edu.sjsu.cmpe275.lab2.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.hibernate.converter.HibernatePersistentCollectionConverter;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.model.Views;
import edu.sjsu.cmpe275.lab2.service.FlightService;
import edu.sjsu.cmpe275.lab2.service.PassengerService;
import edu.sjsu.cmpe275.lab2.service.ReservationService;

/**
 * @author Imran
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

	final static Logger logger = Logger.getLogger(FlightController.class);

	@Autowired
	FlightService flightService;

	@Autowired
	PassengerService passengerService;
	
	@Autowired
	ReservationService reservationService;

	@RequestMapping(method = RequestMethod.POST)
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	public ResponseEntity<?> makeReservation(
			@RequestParam("passengerId") Long passengerId,
			@RequestParam("flightLists") List<String> flightNumbers
			) throws JsonProcessingException {

		Passenger passenger = passengerService.getById(passengerId);
		if (passenger == null) {
			String errMsg = "Sorry, the requested passenger with id " + passengerId + " does not exists";
			logger.debug("Sorry, the requested passenger with id " + passengerId + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);			
			return new ResponseEntity<String>(err.getBadRequestError(),responseHeaders,HttpStatus.BAD_REQUEST);
		}
		
		List<Flight> flights = new ArrayList<Flight>();
		List<Long> arrivalTimes = new ArrayList<Long>();
		List<Long> departureTimes = new ArrayList<Long>();
		
		for(String flightNumber : flightNumbers){
			Flight flight = flightService.getById(flightNumber);
			if (flight == null) {
				String errMsg = "Sorry, the requested flight with id " + flightNumber + " does not exists";
				logger.debug("Sorry, the requested flight with id " + flightNumber + " does not exists");
				ErrorJSON err = new ErrorJSON(errMsg);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(MediaType.APPLICATION_JSON);			
				return new ResponseEntity<String>(err.getBadRequestError(),responseHeaders,HttpStatus.BAD_REQUEST);
			}
			
			if(flight.getSeatsLeft()<=0)
			{
				String errMsg = "The total amount of passengers can not exceed the capacity of the reserved plane. Flight is full!";
				ErrorJSON err = new ErrorJSON(errMsg);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
				return new ResponseEntity<String>(err.getBadRequestError(),responseHeaders,HttpStatus.BAD_REQUEST);
			}
			else{
				//flight.setSeatsLeft(flight.getPlane().getCapacity()-1);
				departureTimes.add(Long.parseLong(flight.getDepartureTime().replace("-", "")));
				arrivalTimes.add(Long.parseLong(flight.getArrivalTime().replaceAll("-", "")));
				flights.add(flight);
			}
		}
		
	
		if(flightService.checkIfOverlappingFlightTimes(departureTimes,arrivalTimes))
		{
			String errMsg = "There is an overlap of flight time intervals. Reservation cannot be done";
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getBadRequestError(),responseHeaders,HttpStatus.BAD_REQUEST);
		}
		else
		{
			for(Flight flight:flights)
			{
				flight.setSeatsLeft(flight.getPlane().getCapacity()-1);
			}
		}
		
		Reservation reservation = new Reservation();
		reservation.setPassenger(passenger);
		int totalPrice = 0;
		for(Flight flight : flights){
			totalPrice += flight.getPrice();
		}
		reservation.setPrice(totalPrice);
		reservation.setFlights(flights);
		reservationService.save(reservation);
		logger.debug("Created reservation " + reservation.getOrdernumber());
		
		XStream xs = new XStream();
		xs.registerConverter(new HibernatePersistentCollectionConverter(xs.getMapper()));
		xs.setMode(XStream.NO_REFERENCES);
		xs.alias("passenger", Passenger.class);
		xs.alias("flight", Flight.class);
		xs.alias("reservation", Reservation.class);
		
		
		xs.omitField(Passenger.class, "reservations");
		xs.omitField(Flight.class, "passengers");
		xs.omitField(Flight.class, "reservations");
		xs.omitField(Passenger.class, "flights");
		
		  //http://xstream.10960.n7.nabble.com/HibernateCollectionConverter-question-td1620.html
		xs.addDefaultImplementation(PersistentBag.class, List.class);
		xs.addDefaultImplementation(Timestamp.class, Date.class);
	
		HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<>(xs.toXML(reservation),responseHeaders, HttpStatus.CREATED);
	}
	
	
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.GET)
	public ResponseEntity<?> getReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exists";
			logger.debug("Reserveration with number " + ordernumber + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders, HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Flight: " + reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}
	
	
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{ordernumber}",method = RequestMethod.PUT)
	public ResponseEntity<?> updateReservation(
			@PathVariable("ordernumber") Long ordernumber,
			@RequestParam(value="flightsAdded",required = false) List<String> flightsAdded,
			@RequestParam(value="flightsRemoved",required = false) List<String> flightsRemoved) throws JsonProcessingException{
		
		Reservation existingReservation = reservationService.getById(ordernumber);
		
		if (existingReservation == null) {
			String errMsg = "Sorry, the requested reservation with id " + ordernumber + " does not exists";
			logger.debug("Sorry, the requested reservationn with id " + ordernumber + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders,HttpStatus.NOT_FOUND);
	
		} else {
			
			reservationService.save(existingReservation);
			return new ResponseEntity<Reservation>(existingReservation, HttpStatus.OK);
		}
	}

	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> searchReservation(
			@RequestParam(value="passengerId",required = false) Long passengerId,
			@RequestParam(value="from",required = false) String from,
			@RequestParam(value="to",required = false) String to,
			@RequestParam(value="flightNumber",required = false) String flightNumber) throws JsonProcessingException{
		
		if(passengerId==null && from == null && to == null && flightNumber == null)
		{
			String errMsg = "Please specify at least one parameter to search for reservation";
			logger.debug("Please specify at least one parameter to search for reservation");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getBadRequestError(),responseHeaders,HttpStatus.BAD_REQUEST);
		}
		
		List<Reservation> existingReservations = reservationService.getAll();
		List<Reservation> reservations = new ArrayList<Reservation>(existingReservations);
		Iterator<Reservation> i = reservations.iterator();
		
			if(passengerId!=null){
				while (i.hasNext()) {
					Reservation reservation = i.next();
					if(reservation.getPassenger().getId()!=passengerId)
					{
						i.remove();
					}
				}
			}
			
			
			Iterator<Reservation> j = reservations.iterator();
			if(flightNumber!=null)
			{
				while (j.hasNext()) {
					Reservation reservation = j.next();
					int count =0;
					for(Flight flight:reservation.getFlights())
					{
						if(flight.getNumber().equals(flightNumber))
						{
							count++;
						}
					}
					if(count==0)
						j.remove();
				}
			}
			
			
			Iterator<Reservation> k = reservations.iterator();
			if(from!=null)
			{
				while (k.hasNext()) {
					Reservation reservation = k.next();
					int count =0;
					for(Flight flight:reservation.getFlights())
					{
						if(flight.getFromOrigin().equals(from))
						{
							count++;
						}
					}
					if(count==0)
						k.remove();
				}
			}
			
			Iterator<Reservation> l = reservations.iterator();
			if(to!=null)
			{
				while (l.hasNext()) {
					Reservation reservation = l.next();
					int count =0;
					for(Flight flight:reservation.getFlights())
					{
						if(flight.getToDestination().equals(to))
						{
							count++;
						}
					}
					if(count==0)
						l.remove();
				}
			}
			
			
			XStream xs = new XStream();
			xs.registerConverter(new HibernatePersistentCollectionConverter(xs.getMapper()));
			xs.setMode(XStream.NO_REFERENCES);
			xs.alias("passenger", Passenger.class);
			xs.alias("flight", Flight.class);
			xs.alias("reservation", Reservation.class);
			
			
			xs.omitField(Passenger.class, "reservations");
			xs.omitField(Flight.class, "passengers");
			xs.omitField(Flight.class, "reservations");
			xs.omitField(Passenger.class, "flights");
			
			  //http://xstream.10960.n7.nabble.com/HibernateCollectionConverter-question-td1620.html
			xs.addDefaultImplementation(PersistentBag.class, List.class);
			xs.addDefaultImplementation(Timestamp.class, Date.class);
			
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
			return new ResponseEntity<>(xs.toXML(reservations),responseHeaders, HttpStatus.CREATED);
			
		
	}
	
	
	
	
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exists";
			logger.debug("Reserveration with number " + ordernumber + " does not exists");
			ErrorJSON err = new ErrorJSON(errMsg);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_JSON);
			return new ResponseEntity<String>(err.getNotFoundError(),responseHeaders, HttpStatus.NOT_FOUND);
			
		} else {
			for(Flight flight:reservation.getFlights())
			{
				flight.setSeatsLeft(flight.getSeatsLeft()+1);
			}
			reservationService.delete(ordernumber);
			String errMsg = "Reservation with number " + ordernumber + " is canceled successfully";
			logger.debug("Reservation with number " + ordernumber + " is canceled successfully");
			ErrorJSON err = new ErrorJSON(errMsg);			
			JSONObject jsonVal = new JSONObject(err.getSuccessfulMsg());
			String xmlVal = XML.toString(jsonVal);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(MediaType.APPLICATION_XML);
			return new ResponseEntity<>(xmlVal,responseHeaders,HttpStatus.OK);
		}
	}

	
}
