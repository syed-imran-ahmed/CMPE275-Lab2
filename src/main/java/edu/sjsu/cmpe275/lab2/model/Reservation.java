package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.*;

/**
 * @author Imran
 */
@Entity
@Table(name = "reservation")
public class Reservation implements java.io.Serializable {

	private static final long serialVersionUID = 2L;

	private Long orderNumber;
	private Passenger passenger;
	private int price;
	private List<Flight> flights;

	public Reservation() {
	}

	public Reservation(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Reservation(Long orderNumber, int price, List<Flight> flights, Passenger passenger) {
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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}

	//@OneToOne(cascade = CascadeType.ALL)
	//@JoinTable(name = "passenger_reservation", joinColumns = { @JoinColumn(name = "reservation_ordernumber") }, inverseJoinColumns = { @JoinColumn(name = "passenger_id") })
	public Passenger getPassenger() {
		return passenger;
	}

	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "reservation_flight", joinColumns = { @JoinColumn(name = "reservation_ordernumber") }, inverseJoinColumns = { @JoinColumn(name = "flight_number") })
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("orderNumber: ").append(this.orderNumber).append(", passenger: ").append(this.passenger).append(", price: ")
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
