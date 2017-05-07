package edu.sjsu.cmpe275.lab2.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Passenger;

/**
* <h1> Passenger repository interface</h1>
* Derived from JPA repository for Passenger
* The Passenger repository interface provides the methods to interact 
* with the ORM without using the queries
*
* @author  Poojith Reddy
* @version 1.0
* @since   2017-04-25
*/ 

@Repository
@Transactional
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

	
	/**
	 * @param phone
	 * @return return the passenger who's phone number is a match with the parameter
	 */
	@Query("SELECT p FROM Passenger p WHERE LOWER(p.phone) = LOWER(:phone)")
    public List<Passenger> findByPhone(@Param("phone") String phone);
}
