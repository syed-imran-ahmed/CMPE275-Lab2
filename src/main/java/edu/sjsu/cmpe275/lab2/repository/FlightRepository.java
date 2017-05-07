package edu.sjsu.cmpe275.lab2.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Flight;



/**
* <h1> Flight repository interface</h1>
* Derived from JPA repository for Flight
* The Flight repository interface provides the methods to interact 
* with the ORM without using the queries
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-26
*/ 

@Repository
@Transactional
public interface FlightRepository extends JpaRepository<Flight, String> {

}
