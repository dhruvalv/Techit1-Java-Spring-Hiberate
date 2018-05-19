package techit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "units")
public class Unit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    private String email;

    private String phone;

    private String description;

    @ManyToMany
    @JoinTable(name = "unit_supervisors",
        joinColumns = @JoinColumn(name = "unit_id"),
        inverseJoinColumns = @JoinColumn(name = "supervisor_id"))
    private List<User> supervisors;

    @ManyToMany
    @JoinTable(name = "unit_technicians",
        joinColumns = @JoinColumn(name = "unit_id"),
        inverseJoinColumns = @JoinColumn(name = "technician_id"))
    private List<User> technicians;

    public Unit()
    {
        supervisors = new ArrayList<User>();
        technicians = new ArrayList<User>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation( String location )
    {
        this.location = location;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone( String phone )
    {
        this.phone = phone;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public List<User> getSupervisors()
    {
        return supervisors;
    }

    public void setSupervisors( List<User> supervisors )
    {
        this.supervisors = supervisors;
    }

    public List<User> getTechnicians()
    {
        return technicians;
    }

    public void setTechnicians( List<User> technicians )
    {
        this.technicians = technicians;
    }

	/*@Override
	public String toString() {
		return "Unit [id=" + id + ", name=" + name + ", location=" + location + ", email=" + email + ", phone=" + phone
				+ ", description=" + description + ", supervisors=" + supervisors + ", technicians=" + technicians
				+ "]";
	}*/
    
    

}
