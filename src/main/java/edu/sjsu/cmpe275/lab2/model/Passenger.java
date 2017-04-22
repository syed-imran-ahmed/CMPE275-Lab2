package edu.sjsu.cmpe275.lab2.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Imran
 */
@Entity
@Table(name = "passenger")
public class Passenger implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Column(name = "firstname", length = 50)
	private String firstname;

	@Column(name = "lastname", length = 50)
	private String lastname;

	@Column(name = "age")
	private int age;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "phone",unique = true, nullable = false)
	private String phone;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "passenger_reservation", joinColumns = { @JoinColumn(name = "passenger_id") }, inverseJoinColumns = { @JoinColumn(name = "reservation_ordernumber") })
	private List<Reservation> reseravtions;

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
	

	public List<Reservation> getReseravtions() {
		return reseravtions;
	}

	public void setReseravtions(List<Reservation> reseravtions) {
		this.reseravtions = reseravtions;
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
