package edu.sjsu.cmpe275.lab2.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author Imran
 *
 */

@Entity
@Table(name = "plane")
public class Plane implements java.io.Serializable {

	//private static final long serialVersionUID = 4910225916550731446L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "capacity", length = 50)
	private int capacity;
	
	@Column(name = "model", length = 50)
	private String model;
	
	@Column(name = "manufacturer", length = 50)
	private String manufacturer;
	
	@Column(name = "yearOfManufacture")
	private int yearOfManufacture;

	public Plane() {
	}

//	public Plane(Long id) {
//		this.id = id;
//	}

	public Plane(int capacity, String model, String manufacturer, int yearOfManufacture) {
		this.capacity = capacity;
		this.model = model;
		this.manufacturer = manufacturer;
		this.yearOfManufacture = yearOfManufacture;
	}

//	public Plane(String firstname, String lastname, String designation, Integer salary) {
//		this.firstname = firstname;
//		this.lastname = lastname;
//		this.designation = designation;
//		this.salary = salary;
//	}

	

//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("Id: ").append(this.id).append(", firstName: ").append(this.firstname).append(", lastName: ")
//				.append(this.lastname).append(", Designation: ").append(this.designation).append(", Salary: ")
//				.append(this.salary);
//		return sb.toString();
//	}

	public int getCapacity() {
		return capacity;
	}

	@Override
	public String toString() {
		return "Plane [capacity=" + capacity + ", model=" + model + ", manufacturer=" + manufacturer
				+ ", yearOfManufacture=" + yearOfManufacture + "]";
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

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (id == null || obj == null || getClass() != obj.getClass())
//			return false;
//		Plane toCompare = (Plane) obj;
//		return id.equals(toCompare.id);
//	}
//
//	@Override
//	public int hashCode() {
//		return id == null ? 0 : id.hashCode();
//	}

}
