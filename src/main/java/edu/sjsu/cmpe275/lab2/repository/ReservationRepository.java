package edu.sjsu.cmpe275.lab2.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Reservation;


/**
 * 
 * @author Imran
 *
 */


@Transactional
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

}
