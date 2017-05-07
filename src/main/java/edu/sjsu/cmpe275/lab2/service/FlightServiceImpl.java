package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;


/**
* <h1> Flight Service Implementation</h1>
* A concrete class for Flight that provides the Flight services
* and extends the Flight Service interface
* to perform the REST calls
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-24
*/

@Service
public class FlightServiceImpl implements FlightService {

	@Autowired
	private FlightRepository flightRepository;

	@Override
	public Flight save(Flight entity) {
		return flightRepository.save(entity);
	}

	@Override
	public Flight getById(Serializable id) {
		return flightRepository.findOne((String) id);
	}

	@Override
	public List<Flight> getAll() {
		return flightRepository.findAll();
	}

	@Override
	public void delete(Serializable id) {
		flightRepository.delete((String) id);
	}
	
	@Override
	public boolean checkIfOverlappingFlightTimes(List<Long> departureTimes, List<Long> arrivalTimes)
	{
		Long[] dep = departureTimes.toArray(new Long[0]);
		Long[] arr = arrivalTimes.toArray(new Long[0]);
		Arrays.sort(dep);
		Arrays.sort(arr);
	
		for(int i=1;i<dep.length;i++)
		{
			if(arr[i-1]>dep[i])
			{
				return true;
			}
		}
		return false;
	}
}
