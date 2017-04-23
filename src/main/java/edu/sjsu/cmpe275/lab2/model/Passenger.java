package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
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
	private Long id;
	private String firstname;
	private String lastname;
	private int age;
	private String gender;
	private String phone;

	//@OneToMany(cascade = CascadeType.ALL)
	//@JoinTable(name = "passenger_reservation", joinColumns = { @JoinColumn(name = "passenger_id") }, inverseJoinColumns = { @JoinColumn(name = "reservation_ordernumber") })
	
	private List<Reservation> reservations;

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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
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
