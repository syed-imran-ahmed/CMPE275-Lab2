package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Imran
 */
@Entity
@Table(name = "passenger")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
public class Passenger implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	//@Column(name = "id", unique = true, nullable = false)
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private Long id;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String firstname;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String lastname;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private int age;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String gender;
	@JsonView({Views.ProjectOnlyPassengerFields.class,Views.ProjectOnlyFlightFieldsInReservation.class,Views.ProjectRelevantFieldsInPassenger.class})
	private String phone;

	//@OneToMany(cascade = CascadeType.ALL)
	//@JoinTable(name = "passenger_reservation", joinColumns = { @JoinColumn(name = "passenger_id") }, inverseJoinColumns = { @JoinColumn(name = "reservation_ordernumber") })
	@JsonView({Views.ProjectRelevantFieldsInPassenger.class})
	private List<Reservation> reservations;
	private List<Flight> flights;

	public Passenger() {
	}

	public Passenger(Long id) {
		this.id = id;
	}

	public Passenger(Long id, String firstname, String lastname, int age, String gender, String phone) {
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
	}

	public Passenger(String firstname, String lastname, int age, String gender, String phone) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.gender = gender;
		this.phone = phone;
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(unique=true)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@OneToMany(orphanRemoval=true,mappedBy = "passenger", cascade = CascadeType.ALL)
	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	@ManyToMany(mappedBy="passengers")
	@JsonIgnore
	public List<Flight> getFlights() {
		return flights;
	}

	public void setFlights(List<Flight> flights) {
		this.flights = flights;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Id: ").append(this.id).append(", firstName: ").append(this.firstname).append(", lastName: ")
				.append(this.lastname).append(", Age: ").append(this.age).append(", Gender: ")
				.append(this.gender).append(", Phone: ").append(this.phone);
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (id == null || obj == null || getClass() != obj.getClass())
			return false;
		Passenger toCompare = (Passenger) obj;
		return id.equals(toCompare.id) && phone.equals(toCompare.phone);
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

}
