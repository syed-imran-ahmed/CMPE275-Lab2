package edu.sjsu.cmpe275.lab2.service;

import java.util.List;

import edu.sjsu.cmpe275.lab2.model.Flight;

/**
 * 
 * @author Imran
 *
 */
public interface FlightService extends CRUDService<Flight> {

	public boolean checkIfOverlappingFlightTimes(List<Long> departureTimes, List<Long> arrivalTimes);
}
