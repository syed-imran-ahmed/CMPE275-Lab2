package edu.sjsu.cmpe275.lab2.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.collection.internal.PersistentBag;
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
* <h1>Reservation Endpoints</h1>
* The reservation controller class provides the REST endpoints
* to map the basic GET,POST,PUT and DELETE request for
* its corresponding operations along with search reservation
*
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-26
*/ 

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	final static Logger logger = Logger.getLogger(ReservationController.class);

	@Autowired
	FlightService flightService;

	@Autowired
	PassengerService passengerService;

	@Autowired
	ReservationService reservationService;

	/**
	 * @param passengerId
	 * @param flightNumbers
	 * @return Return the created reservation in XML or else a proper error message
	 * @throws JsonProcessingException
	 */
	@RequestMapping(method = RequestMethod.POST)
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	public ResponseEntity<?> makeReservation(
			@RequestParam("passengerId") Long passengerId,
			@RequestParam("flightLists") List<String> flightNumbers
			) throws JsonProcessingException {

		Passenger passenger = passengerService.getById(passengerId);
		if (passenger == null) {
			String errMsg = "Sorry, the requested passenger with id " + passengerId + " does not exist";
			logger.debug("Sorry, the requested passenger with id " + passengerId + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}

		List<String> dupFlightNum = new ArrayList<String>(flightNumbers);
		List<Flight> flights = new ArrayList<Flight>();
		List<Reservation> passengerReservations = passenger.getReservations();
		
		for(Reservation res : passengerReservations)
		{
			for(Flight flight: res.getFlights()){
				dupFlightNum.add(flight.getNumber());
			}
		}

		ResponseEntity<?> res = checkFlightOverlapping(dupFlightNum);
		if(res!=null)
			return res;
		
		
		for(String id : flightNumbers)
		{
			Flight flight = flightService.getById(id);
			if(flight.getSeatsLeft()<=0)
			{
				String errMsg = "The total amount of passengers can not exceed the capacity of the reserved plane. Flight is full!";
				return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
			}
			else
			{
				flights.add(flight);
				flight.setSeatsLeft(flight.getSeatsLeft()-1);
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


	/**
	 * @param ordernumber
	 * @return return the Reservation in JSON or error proper message 
	 * @throws JsonProcessingException
	 */
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.GET)
	public ResponseEntity<?> getReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exist";
			logger.debug("Reserveration with number " + ordernumber + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
		}
		logger.debug("Found Flight: " + reservation);
		return new ResponseEntity<Reservation>(reservation, HttpStatus.OK);
	}


	/**
	 * @param ordernumber
	 * @param flightsAdded
	 * @param flightsRemoved
	 * @return Return the updated reservation or else a proper error message
	 * @throws JsonProcessingException
	 */
	@JsonView(Views.ProjectOnlyFlightFieldsInReservation.class)
	@RequestMapping(value = "/{ordernumber}",method = {RequestMethod.PUT, RequestMethod.POST})
	public ResponseEntity<?> updateReservation(
			@PathVariable("ordernumber") Long ordernumber,
			@RequestParam(value="flightsAdded",required = false) List<String> flightsAdded,
			@RequestParam(value="flightsRemoved",required = false) List<String> flightsRemoved) throws JsonProcessingException{

		Reservation existingReservation = reservationService.getById(ordernumber);

		if (existingReservation == null) {
			String errMsg = "Sorry, the requested reservation with id " + ordernumber + " does not exist";
			logger.debug("Sorry, the requested reservationn with id " + ordernumber + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);

		} else if((flightsAdded!=null && flightsAdded.size()<=0) || (flightsRemoved!=null && flightsRemoved.size()<=0)){

			String errMsg = "Sorry, the flights list cannot be empty";
			logger.debug("Sorry, the flights list cannot be empty");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}
		else if(flightsAdded!=null || flightsRemoved!=null)
		{
			//First remove the flights then check for the overlap between the remaining flights and newly added flights
			List<Flight> f =  existingReservation.getFlights();
			if(flightsRemoved!=null){
				Iterator<Flight> i = f.iterator();
				while(i.hasNext())
				{
					Flight flight = i.next();
					for(String s:flightsRemoved)
					{
						if(flight.getNumber().equals(s))
						{
							i.remove();
							flight.setSeatsLeft(flight.getSeatsLeft()+1); // updating the seats left if the flight is removed from a reservation
							break;
						}
					}
				}
			}

			List<Flight> flights = new ArrayList<Flight>(f);

			if(flightsAdded!=null && flightsAdded.size()!=0)
			{
				List<String> dupFlightNum = new ArrayList<String>(flightsAdded);

				for(Flight flight: flights){
					dupFlightNum.add(flight.getNumber());
				}

				ResponseEntity<?> res = checkFlightOverlapping(dupFlightNum);
				if(res!=null)
					return res;


				for(String id : flightsAdded)
				{
					Flight flight = flightService.getById(id);
					if (flight == null) {
						String errMsg = "Sorry, the requested flight to be added with id " + id + " does not exist";
						logger.debug("Sorry, the requested flight to be added with id " + id + " does not exist");
						return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);
					}
					if(flight.getSeatsLeft()<=0)
					{
						String errMsg = "The total amount of passengers can not exceed the capacity of the reserved plane. Flight is full!";
						return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
					}
					else
					{
						flights.add(flight);
						flight.setSeatsLeft(flight.getSeatsLeft()-1);
					}
				}
			}

			existingReservation.setFlights(flights);
			int totalPrice = 0;
			for(Flight flight : flights){
				totalPrice += flight.getPrice();
			}
			existingReservation.setPrice(totalPrice);
			reservationService.save(existingReservation);
			return new ResponseEntity<Reservation>(existingReservation, HttpStatus.OK);
		}

		return new ResponseEntity<Reservation>(existingReservation, HttpStatus.OK);

	}

	/**
	 * @param passengerId
	 * @param from
	 * @param to
	 * @param flightNumber
	 * @return Return the search reservation result in xml
	 * @throws JsonProcessingException
	 */
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
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}

		List<Reservation> reservations = reservationService.searchReservations(passengerId, from, to, flightNumber);

		XStream xs = new XStream();
		xs.registerConverter(new HibernatePersistentCollectionConverter(xs.getMapper()));
		xs.setMode(XStream.NO_REFERENCES);
		xs.alias("passenger", Passenger.class);
		xs.alias("flight", Flight.class);
		xs.alias("reservation", Reservation.class);
		xs.alias("reservations", List.class);


		xs.omitField(Passenger.class, "reservations");
		xs.omitField(Flight.class, "passengers");
		xs.omitField(Flight.class, "reservations");
		xs.omitField(Passenger.class, "flights");

		//http://xstream.10960.n7.nabble.com/HibernateCollectionConverter-question-td1620.html
		xs.addDefaultImplementation(PersistentBag.class, List.class);
		xs.addDefaultImplementation(Timestamp.class, Date.class);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_XML);
		return new ResponseEntity<>(xs.toXML(reservations),responseHeaders, HttpStatus.OK);
	}


	/**
	 * @param ordernumber
	 * @return Return the deleted order number or proper error message
	 * @throws JsonProcessingException
	 */
	@RequestMapping(value = "/{ordernumber}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteReservation(@PathVariable("ordernumber") Long ordernumber) throws JsonProcessingException {
		Reservation reservation = reservationService.getById(ordernumber);
		if (reservation == null) {
			String errMsg = "Reserveration with number " + ordernumber + " does not exist";
			logger.debug("Reserveration with number " + ordernumber + " does not exist");
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.NOT_FOUND);

		} else {
			for(Flight flight:reservation.getFlights())
			{
				flight.setSeatsLeft(flight.getSeatsLeft()+1);
			}
			reservationService.delete(ordernumber);
			String successMsg = "Reservation with number " + ordernumber + " is cancelled successfully";
			logger.debug("Reservation with number " + ordernumber + " is cancelled successfully");
			return ControllerUtil.sendSuccess(successMsg);
		}
	}


	/**
	 * @param flightNumbers
	 * @return Return the error message if the flight times overlap
	 * @throws JsonProcessingException
	 */
	ResponseEntity<?> checkFlightOverlapping(List<String> flightNumbers) throws JsonProcessingException
	{
		List<Long> arrivalTimes = new ArrayList<Long>();
		List<Long> departureTimes = new ArrayList<Long>();

		for(String flightNumber : flightNumbers){
			Flight flight = flightService.getById(flightNumber);
			if (flight == null) {
				String errMsg = "Sorry, the requested flight with id " + flightNumber + " does not exist";
				logger.debug("Sorry, the requested flight with id " + flightNumber + " does not exist");
				return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
			}
			else{
				departureTimes.add(Long.parseLong(flight.getDepartureTime().replace("-", "")));
				arrivalTimes.add(Long.parseLong(flight.getArrivalTime().replaceAll("-", "")));
			}
		}

		if(flightService.checkIfOverlappingFlightTimes(departureTimes,arrivalTimes))
		{
			String errMsg = "There is an overlap of flight time intervals. Reservation cannot be done";
			return ControllerUtil.sendBadRequest(errMsg, HttpStatus.BAD_REQUEST);
		}
		return null;
	}


}
