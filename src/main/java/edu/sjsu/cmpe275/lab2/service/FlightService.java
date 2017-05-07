package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Flight;


/**
* <h1> Flight Service Interface</h1>
* An Interface that provides the flight services
* and extends the CRUDservice
* to perform the REST calls
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-23
*/ 

public interface FlightService extends CRUDService<Flight> {

	/**
	 * @param departureTimes
	 * @param arrivalTimes
	 * @return 
	 */
	public boolean checkIfOverlappingFlightTimes(List<Long> departureTimes, List<Long> arrivalTimes);
}
