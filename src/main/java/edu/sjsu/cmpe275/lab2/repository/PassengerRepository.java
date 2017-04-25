package edu.sjsu.cmpe275.lab2.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Passenger;
/**
 * 
 * @author Imran
 *
 */

@Repository
@Transactional
public interface PassengerRepository extends JpaRepository<Passenger, Long> {

	
	@Query("SELECT p FROM Passenger p WHERE LOWER(p.phone) = LOWER(:phone)")
    public List<Passenger> findByPhone(@Param("phone") String phone);
}
