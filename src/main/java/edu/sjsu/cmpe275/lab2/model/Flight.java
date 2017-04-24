package edu.sjsu.cmpe275.lab2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Imran
 *
 */

@Entity
@Table(name = "flight")
public class Flight implements java.io.Serializable {

	private static final long serialVersionUID = 3L;

	private String number;
	private int price;
	private String from;
	private String to;
	private Date departureTime;
	private Date arrivalTime;
	private int seatsLeft;
	private String description;
	@Embedded
	private Plane plane;
	private List<Passenger> passengers;

	private List<Reservation> reservations;

    @Id
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getFromOrigin() {
		return from;
	}

	public void setFromOrigin(String from) {
		this.from = from;
	}

	public String getToDestination() {
		return to;
	}

	public void setToDestination(String to) {
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

	@ManyToMany(mappedBy="flights")
	@JsonIgnore
	public List<Reservation> getReservations() {
		return reservations;
	}
	
	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "flight_passenger", joinColumns = { @JoinColumn(name = "flight_number", referencedColumnName="number") }, inverseJoinColumns = { @JoinColumn(name = "passenger_id", referencedColumnName="id") })
	public List<Passenger> getPassengers() {
		
		if(getReservations()!=null)
		{
			for(Reservation res:getReservations())
			{
				passengers.add(res.getPassenger());
			}
		}
		return passengers;
	}

	public void setPassengers(List<Passenger> passengers) {
		
		
		this.passengers = passengers;
	}
	
//	@OneToMany(cascade = CascadeType.ALL)
//	@JoinTable(name = "flight_passenger", joinColumns = { @JoinColumn(name = "flight_number") }, inverseJoinColumns = { @JoinColumn(name = "passenger_id") })
//	public List<Passenger> getPassengers() {
//		return passengers;
//	}
//
//	public void setPassengers(List<Passenger> passengers) {
//		this.passengers = passengers;
//	}

	@Override
	public String toString() {
		return "Flight [number=" + number + ", price=" + price + ", from=" + from + ", to=" + to + ", departureTime="
				+ departureTime + ", arrivalTime=" + arrivalTime + ", seatsLeft=" + seatsLeft + ", description="
				+ description + "]";
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
