package edu.sjsu.cmpe275.lab2.model;

import javax.persistence.Embeddable;

import com.fasterxml.jackson.annotation.JsonView;

/**
* <h1>Model Class for Plane</h1>
* The plane model class provides the view
* of the plane and its attributes along with how the 
* fields will be projected using views.
* This object will be embedded in the Flight model
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-23
*/ 

@Embeddable
public class Plane implements java.io.Serializable {

	private static final long serialVersionUID = 4L;

	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private int capacity;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String model;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String manufacturer;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private int yearOfManufacture;

	public Plane() {
	}

	public Plane(int capacity, String model, String manufacturer, int yearOfManufacture) {
		this.capacity = capacity;
		this.model = model;
		this.manufacturer = manufacturer;
		this.yearOfManufacture = yearOfManufacture;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public int getYearOfManufacture() {
		return yearOfManufacture;
	}

	public void setYearOfManufacture(int yearOfManufacture) {
		this.yearOfManufacture = yearOfManufacture;
	}

	@Override
	public String toString() {
		return "Plane [capacity=" + capacity + ", model=" + model + ", manufacturer=" + manufacturer
				+ ", yearOfManufacture=" + yearOfManufacture + "]";
	}
}
