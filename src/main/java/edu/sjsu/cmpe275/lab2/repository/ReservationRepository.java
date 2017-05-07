package edu.sjsu.cmpe275.lab2.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Reservation;


/**
* <h1> Reservation repository interface</h1>
* Derived from JPA repository for Reservation
* The Reservation repository interface provides the methods to interact 
* with the ORM without using the queries
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-25
*/ 

@Transactional
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long>{

    /**
     * @param passengerId
     * @param from
     * @param to
     * @param flightNumber
     * @return Search the flights with the given parameters using AND and if some of the 
     * parameter is missing then take them as null and give the results
     */
    @Query("select DISTINCT(R) "+ 
    " from Reservation R "+
    " JOIN R.flights F "+
    " JOIN R.passenger P "+
    " where ((:flightNumber IS NULL) OR (:flightNumber IS NOT NULL AND F.number = :flightNumber)) "+
    " and ((:from IS NULL) OR (:from IS NOT NULL AND F.fromOrigin = :from)) "+
    " and ((:to IS NULL) OR (:to IS NOT NULL AND F.toDestination = :to)) "+
    " and ((:passengerId IS NULL) OR (:passengerId IS NOT NULL AND P.id = :passengerId))")
    public List<Reservation> searchReservations(@Param("passengerId") Long passengerId
    		,@Param("from") String from
    		,@Param("to") String to
    		,@Param("flightNumber") String flightNumber);
}
