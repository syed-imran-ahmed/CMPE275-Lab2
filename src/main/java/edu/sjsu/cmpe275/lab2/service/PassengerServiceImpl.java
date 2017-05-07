package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;

/**
* <h1> Passenger Service Implementation</h1>
* A concrete class for Passenger that provides the passenger services
* and extends the Passenger Service interface
* to perform the REST calls
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-24
*/

@Service
public class PassengerServiceImpl implements PassengerService {

	@Autowired
	private PassengerRepository passengerRepository;

	@Override
	public Passenger save(Passenger entity) {
		return passengerRepository.save(entity);
	}

	@Override
	public Passenger getById(Serializable id) {
		return passengerRepository.findOne((Long) id);
	}
	
	@Override
	public List<Passenger> getByPhone(Serializable phone){
		return passengerRepository.findByPhone((String) phone);
	}

	@Override
	public List<Passenger> getAll() {
		return passengerRepository.findAll();
	}

	@Override
	public void delete(Serializable id) {
		passengerRepository.delete((Long) id);
	}

}
