package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Reservation;


/**
* <h1> Reservation Service Interface</h1>
* An Interface that provides the Reservation services
* and extends the CRUDservice
* to perform the REST calls
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-24
*/ 
public interface ReservationService extends CRUDService<Reservation>{

	List<Reservation> searchReservations(Long passengerId, String from, String to, String flightNumber);
}
