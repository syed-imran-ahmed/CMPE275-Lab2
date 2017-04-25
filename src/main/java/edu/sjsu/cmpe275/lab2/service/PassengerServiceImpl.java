package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.sjsu.cmpe275.lab2.model.Passenger;
import edu.sjsu.cmpe275.lab2.repository.PassengerRepository;

/**
 * 
 * @author Imran
 *
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
