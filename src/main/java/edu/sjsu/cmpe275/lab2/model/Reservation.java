package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
* <h1>Model Class for Reservation</h1>
* The Reservation model class provides the entity view
* of the reservation and its attributes along with how the 
* fields will be projected using views
*
* @author  Syed Imran Ahmed
* @version 1.0
* @since   2017-04-26
*/ 

@Entity
@Table(name = "reservation")
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=JsonTypeInfo.Id.NAME)
public class Reservation implements java.io.Serializable {

	private static final long serialVersionUID = 2L;

	@JsonView({Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private Long ordernumber;
	@JsonView({Views.ProjectOnlyFlightFieldsInReservation.class})
	private Passenger passenger;
	@JsonView({Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private int price;
	@JsonView({Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private List<Flight> flights;

	public Reservation() {
	}

	public Reservation(Long ordernumber) {
		this.ordernumber = ordernumber;
	}

	public Reservation(Long ordernumber, int price, List<Flight> flights, Passenger passenger) {
		this.ordernumber = ordernumber;
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
	public Long getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(Long ordernumber) {
		this.ordernumber = ordernumber;
	}

	@ManyToOne
	@JoinColumn(name = "passenger_id")
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

	@ManyToMany(cascade  = 
        { 
                CascadeType.DETACH, 
                CascadeType.MERGE, 
                CascadeType.REFRESH, 
                CascadeType.PERSIST,
        })
	@JoinTable(name = "reservation_flight", joinColumns = { @JoinColumn(name = "reservation_ordernumber", referencedColumnName="ordernumber") }, inverseJoinColumns = { @JoinColumn(name = "flight_number", referencedColumnName="number") })
    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("ordernumber: ").append(this.ordernumber).append(", passenger: ").append(this.passenger).append(", price: ")
				.append(this.price);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (ordernumber == null || obj == null || getClass() != obj.getClass())
			return false;
		Reservation toCompare = (Reservation) obj;
		return ordernumber.equals(toCompare.ordernumber);
	}

	@Override
	public int hashCode() {
		return ordernumber == null ? 0 : ordernumber.hashCode();
	}

}
