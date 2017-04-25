package edu.sjsu.cmpe275.lab2.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.sjsu.cmpe275.lab2.model.Reservation;


/**
 * 
 * @author Imran
 *
 */


@Transactional
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
