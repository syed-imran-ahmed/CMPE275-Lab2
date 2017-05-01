package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Reservation;

public interface ReservationService extends CRUDService<Reservation>{

	List<Reservation> searchReservations(Long passengerId, String from, String to, String flightNumber);
}
