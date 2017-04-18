package edu.sjsu.cmpe275.lab2.model;

import java.util.Date;
import java.util.List;

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
@Table(name = "flight")
public class Flight implements java.io.Serializable {

	//private static final long serialVersionUID = 4910225916550731446L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "number", unique = true, nullable = false)
	private String number;

	@Column(name = "price")
	private int price;

	@Column(name = "from", length = 50)
	private String from;

	@Column(name = "to", length = 50)
	private String to;

	@Column(name = "departureTime")
	private Date departureTime;
	
	@Column(name = "arrivalTime")
	private Date arrivalTime;
	
	@Column(name = "seatsLeft")
	private int seatsLeft;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "plane")
	private Plane plane; // Embedded
	
	private List<Passenger> passengers;
	
	

	public Flight() {
	}

	public Flight(String number) {
		this.number = number;
	}

	public Flight(String number, int price, String from, String to, Date departureTime, Date arrivalTime,
			String seatsLeft, String description, Plane plane, List<Passenger> passengers ) {
		this.number = number;
		this.price = price;
		this.from = from;
		this.to = to;
		this.description = description;
		this.plane = plane;
		this.passengers = passengers;
	}

	public Flight(int price, String from, String to, Date departureTime, Date arrivalTime,
			String seatsLeft, String description, Plane plane, List<Passenger> passengers) {
		this.price = price;
		this.from = from;
		this.to = to;
		this.description = description;
		this.plane = plane;
		this.passengers = passengers;
	}

	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPrice() {
		return price;
	}

	public void setPricce(int price) {
		this.price = price;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	public Date getArrivalTime() {
		return arrivalTime;
	}

	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}

	public int getSeatsLeft() {
		return seatsLeft;
	}

	public void setSeatsLeft(int seatsLeft) {
		this.seatsLeft = seatsLeft;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Plane getPlane() {
		return plane;
	}

	public void setPlane(Plane plane) {
		this.plane = plane;
	}

	public List<Passenger> getPassengers() {
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		this.passengers = passengers;
	}

	
	
//	@Override
//	public String toString() {
//		StringBuffer sb = new StringBuffer();
//		sb.append("Id: ").append(this.id).append(", firstName: ").append(this.firstname).append(", lastName: ")
//				.append(this.lastname).append(", Designation: ").append(this.designation).append(", Salary: ")
//				.append(this.salary);
//		return sb.toString();
//	}

	@Override
	public String toString() {
		return "Flight [number=" + number + ", pricce=" + price + ", from=" + from + ", to=" + to + ", departureTime="
				+ departureTime + ", arrivalTime=" + arrivalTime + ", seatsLeft=" + seatsLeft + ", description="
				+ description + ", passengers=" + passengers + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (number == null || obj == null || getClass() != obj.getClass())
			return false;
		Flight toCompare = (Flight) obj;
		return number.equals(toCompare.number);
	}

	@Override
	public int hashCode() {
		return number == null ? 0 : number.hashCode();
	}

}
