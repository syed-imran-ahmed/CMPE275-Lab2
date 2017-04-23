package edu.sjsu.cmpe275.lab2.model;

import javax.persistence.Embeddable;

/**
 * 
 * @author Imran
 *
 */

@Embeddable
public class Plane implements java.io.Serializable {

	private static final long serialVersionUID = 4L;

	private int capacity;
	private String model;
	private String manufacturer;
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
