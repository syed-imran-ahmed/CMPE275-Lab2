package edu.sjsu.cmpe275.lab2.service;

import java.io.Serializable;
import java.util.List;

/**
* <h1> CRUD Service Interface</h1>
* An Interface that provides the services to mandate the implementation 
* to perform the REST calls
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-23
*/ 

public interface CRUDService<E> {

	E save(E entity);

	E getById(Serializable id);

	List<E> getAll();

	void delete(Serializable id);
}
