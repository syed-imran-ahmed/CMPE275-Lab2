package edu.sjsu.cmpe275.lab2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.sjsu.cmpe275.lab2.model.Employee;
/**
 * 
 * @author Imran
 *
 */

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
