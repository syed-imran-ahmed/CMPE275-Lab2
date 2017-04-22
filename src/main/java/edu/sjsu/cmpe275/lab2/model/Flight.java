package edu.sjsu.cmpe275.lab2.model;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

/**
 * 
 * @author Imran
 *
 */

@Entity
@Table(name = "flight")
public class Flight implements java.io.Serializable {

	//private static final long serialVersionUID = 4910225916550731446L;
	
	
	private Plane plane;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;
	
	@Column(name = "number", unique = true, length = 50,nullable = false)
	private String number;

	@Column(name = "price")
	private int price;

	@Column(name = "from_origin", length = 50)
	private String from_origin;

	@Column(name = "to_destination", length = 50)
	private String to_destination;

	@Column(name = "departureTime")
	private Date departureTime;
	
	@Column(name = "arrivalTime")
	private Date arrivalTime;
	
	@Column(name = "seatsLeft")
	private int seatsLeft;
	
	@Column(name = "description")
	private String description;
	
	//@Column(name = "plane")
	 // Embedded
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "flight_passenger", joinColumns = { @JoinColumn(name = "flight_number") }, inverseJoinColumns = { @JoinColumn(name = "passenger_id") })
	private List<Passenger> passengers;
	
//	@OneToMany(mappedBy = "flight", cascade = CascadeType.ALL)
//	private List<Passenger> passengers;
	
	

	public Flight() {
	}

	public Flight(String number) {
		this.number = number;
	}

	public Flight(String number, int price, String from_origin, String to_destination, Date departureTime, Date arrivalTime,
			int seatsLeft, String description, Plane plane ) {
		this.number = number;
		this.price = price;
		this.from_origin = from_origin;
		this.to_destination = to_destination;
		this.departureTime = departureTime;
		this.arrivalTime = arrivalTime;
		this.seatsLeft = seatsLeft;
		this.description = description;
		this.plane = plane;
	}

	public Flight(int price, String from_origin, String to_destination, Date departureTime, Date arrivalTime,
			int seatsLeft, String description, Plane plane, List<Passenger> passengers) {
		this.price = price;
		this.from_origin = from_origin;
		this.to_destination = to_destination;
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
		return from_origin;
	}

	public void setFrom(String from_origin) {
		this.from_origin = from_origin;
	}

	public String getTo() {
		return to_destination;
	}

	public void setTo(String to_destination) {
		this.to_destination = to_destination;
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

	@OneToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "flight_plane", joinColumns = @JoinColumn(name = "flight_id"), inverseJoinColumns = @JoinColumn(name = "plane_id"))
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
	
	
//	@ManyToOne
//	@JoinColumn(name = "flight_reservation_id")

	
	
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
		return "Flight [number=" + number + ", pricce=" + price + ", from=" + from_origin + ", to=" + to_destination + ", departureTime="
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
