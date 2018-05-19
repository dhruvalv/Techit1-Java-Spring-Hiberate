package techit.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name = "users")
public class User implements Serializable {

	public enum Type {
		REGULAR, ADMIN, SUPERVISOR, TECHNICIAN
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(nullable = false, unique = true)
	private String username;

	// This field is used for getting a password in plain text from a user
	// when adding/editing a user. It is NOT stored in database.
	@JsonProperty(access = Access.WRITE_ONLY)
	@Transient
	private String password;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String hash;

	private boolean enabled = true;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(nullable = false, unique = true)
	private String email;

	private String phone;

	private String department;

	@JsonIgnore
	@ManyToOne
	private Unit unit;

	public User() {
	}

	// Use unitId instead of unit in serialization
	public Long getUnitId() {
		return unit != null ? unit.getId() : null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

/*	@Override
	public String toString() {
		return "User [id=" + id + ", type=" + type + ", username=" + username + ", enabled=" + enabled + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", phone=" + phone + ", department="
				+ department + ", unit=" + unit != null ? unit.getId().toString() : unit + "]";
	}*/
}
