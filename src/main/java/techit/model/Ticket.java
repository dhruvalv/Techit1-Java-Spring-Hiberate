package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "tickets")
public class Ticket implements Serializable {

	public enum Priority {
		LOW, MEDIUM, HIGH
	}

	public enum Status {
		OPEN, ASSIGNED, ONHOLD, COMPLETED, ClOSED
	}

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(name = "created_by", nullable = false)
	private User createdBy;

	@Column(name = "created_for_name")
	private String createdForName;

	@Column(name = "created_for_email", nullable = false)
	private String createdForEmail;

	@Column(name = "created_for_phone")
	private String createdForPhone;

	@Column(name = "created_for_department")
	private String createdForDepartment;

	@Column(nullable = false)
	private String subject;

	private String details;

	private String location;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Unit unit;

	@ManyToMany
	@JoinTable(name = "ticket_technicians", joinColumns = @JoinColumn(name = "ticket_id"), inverseJoinColumns = @JoinColumn(name = "technician_id"))
	private List<User> technicians;

	@JsonManagedReference
	@OneToMany(mappedBy = "ticket")
	private List<Update> updates;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "date_created")
	private Date dateCreated;

	@Column(name = "date_assigned")
	private Date dateAssigned;

	@Column(name = "date_updated")
	private Date dateUpdated;

	@Column(name = "date_closed")
	private Date dateClosed;

	public Ticket() {
		priority = Priority.MEDIUM;
		status = Status.OPEN;
		technicians = new ArrayList<User>();
		updates = new ArrayList<Update>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedForName() {
		return createdForName;
	}

	public void setCreatedForName(String createdForName) {
		this.createdForName = createdForName;
	}

	public String getCreatedForEmail() {
		return createdForEmail;
	}

	public void setCreatedForEmail(String createdForEmail) {
		this.createdForEmail = createdForEmail;
	}

	public String getCreatedForPhone() {
		return createdForPhone;
	}

	public void setCreatedForPhone(String createdForPhone) {
		this.createdForPhone = createdForPhone;
	}

	public String getCreatedForDepartment() {
		return createdForDepartment;
	}

	public void setCreatedForDepartment(String createdForDepartment) {
		this.createdForDepartment = createdForDepartment;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public List<User> getTechnicians() {
		return technicians;
	}

	public void setTechnicians(List<User> technicians) {
		this.technicians = technicians;
	}

	public List<Update> getUpdates() {
		return updates;
	}

	public void setUpdates(List<Update> updates) {
		this.updates = updates;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateAssigned() {
		return dateAssigned;
	}

	public void setDateAssigned(Date dateAssigned) {
		this.dateAssigned = dateAssigned;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Date getDateClosed() {
		return dateClosed;
	}

	public void setDateClosed(Date dateClosed) {
		this.dateClosed = dateClosed;
	}

	/*@Override
	public String toString() {
		return "Ticket [id=" + id + ", createdBy=" + createdBy != null ? createdBy.getId().toString()
				: createdBy + ", createdForName=" + createdForName + ", createdForEmail=" + createdForEmail
						+ ", createdForPhone=" + createdForPhone + ", createdForDepartment=" + createdForDepartment
						+ ", subject=" + subject + ", details=" + details + ", location=" + location + ", unit="
						+ unit != null
								? unit.getId().toString()
								: unit + ", technicians=" + technicians + ", updates=" + updates + ", priority="
										+ priority + ", status=" + status + ", dateCreated=" + dateCreated
										+ ", dateAssigned=" + dateAssigned + ", dateUpdated=" + dateUpdated
										+ ", dateClosed=" + dateClosed + "]";
	}*/

}
