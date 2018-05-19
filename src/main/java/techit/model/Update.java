package techit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "updates")
public class Update implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@JsonBackReference
	@ManyToOne
	private Ticket ticket;

	@ManyToOne
	private User technician;

	private String details;

	private Date date;

	public Update() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public User getTechnician() {
		return technician;
	}

	public void setTechnician(User technician) {
		this.technician = technician;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
/*
	@Override
	public String toString() {
		return "Update [id=" + id + ", ticket=" + ticket != null ? ticket.getId().toString()
				: ticket + ", technician=" + technician.getId() + ", details=" + details + ", date=" + date + "]";
	}*/

}
