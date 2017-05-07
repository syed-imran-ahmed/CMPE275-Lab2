package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lab2.model.Reservation;
import edu.sjsu.cmpe275.lab2.repository.ReservationRepository;



/**
* <h1> Reservation Service Implementation</h1>
* A concrete class for Reservation that provides the Reservation services
* and extends the Resservation Service interface
* to perform the REST calls
*
* @author  Poojitha REddy
* @version 1.0
* @since   2017-04-22
*/

@Service
public class ReservationServiceImpl implements ReservationService {

	@Autowired
	ReservationRepository reservationRepository;

	@Override
	public Reservation save(Reservation entity) {
		return reservationRepository.save(entity);
	}

	@Override
	public Reservation getById(Serializable id) {
		return reservationRepository.findOne((Long) id);
	}

	@Override
	public List<Reservation> getAll() {
		return reservationRepository.findAll();
	}

	@Override
	public void delete(Serializable id) {
		reservationRepository.delete((Long) id);
	}

	@Override
	public List<Reservation> searchReservations(Long passengerId, String from, String to, String flightNumber) {
		return reservationRepository.searchReservations(passengerId, from, to, flightNumber);
	}
}
