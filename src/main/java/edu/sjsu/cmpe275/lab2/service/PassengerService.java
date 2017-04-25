package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Passenger;

/**
 * 
 * @author Imran
 *
 */
public interface PassengerService extends CRUDService<Passenger> {

	public List<Passenger> getByPhone(Serializable phone);
}
