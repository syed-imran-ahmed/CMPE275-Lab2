package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Passenger;

/**
* <h1> Passenger Service Interface</h1>
* An Interface that provides the Passenger services
* and extends the CRUDservice
* to perform the REST calls
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-24
*/ 

public interface PassengerService extends CRUDService<Passenger> {

	public List<Passenger> getByPhone(Serializable phone);
}
