package edu.sjsu.cmpe275.lab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Flight;
/**
 * 
 * @author Imran
 *
 */

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {

}
