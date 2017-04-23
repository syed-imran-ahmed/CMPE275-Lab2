package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lab2.model.Flight;
import edu.sjsu.cmpe275.lab2.repository.FlightRepository;

/**
 * 
 * @author Imran
 *
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
}
