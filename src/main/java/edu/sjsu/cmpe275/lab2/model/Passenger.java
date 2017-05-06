package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
* <h1>Model Class for Passenger</h1>
* The passenger model class provides the entity view
* of the passenger and its attributes along with how the 
* fields will be projected using views
*
* @author  Poojitha Reddy
* @version 1.0
* @since   2017-04-23
*/ 

@Entity
@Table(name = "passenger")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "id")
@JsonTypeInfo(include=As.WRAPPER_OBJECT, use=JsonTypeInfo.Id.NAME)
public class Passenger implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

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

	
	@JsonView({Views.ProjectRelevantFieldsInPassenger.class})
	private List<Reservation> reservations;

	public Passenger() {
	}

	public Passenger(Long id) {
		this.id = id;
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
