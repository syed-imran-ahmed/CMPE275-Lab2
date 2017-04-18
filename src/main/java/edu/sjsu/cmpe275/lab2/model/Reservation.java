package edu.sjsu.cmpe275.lab2.model;

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
@Table(name = "reservation")
public class Reservation implements java.io.Serializable {

	//private static final long serialVersionUID = 4910225916550731446L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "orderNumber", unique = true, nullable = false)
	private String orderNumber;

	@Column(name = "price", length = 50)
	private int price;

	private List<Flight> flights;

	private Passenger passenger;

	public Reservation() {
	}

	public Reservation(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Reservation(String orderNumber, int price, List<Flight> flights, Passenger passenger) {
		this.orderNumber = orderNumber;
		this.price = price;
		this.flights = flights;
		this.passenger = passenger;
	}

	public Reservation(int price, List<Flight> flights, Passenger passenger) {
		this.price = price;
		this.flights = flights;
		this.passenger = passenger;
	}

	

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("OrderNumber: ").append(this.orderNumber).append(", passenger: ").append(this.passenger).append(", price: ")
				.append(this.price);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (orderNumber == null || obj == null || getClass() != obj.getClass())
			return false;
		Reservation toCompare = (Reservation) obj;
		return orderNumber.equals(toCompare.orderNumber);
	}

	@Override
	public int hashCode() {
		return orderNumber == null ? 0 : orderNumber.hashCode();
	}

}
