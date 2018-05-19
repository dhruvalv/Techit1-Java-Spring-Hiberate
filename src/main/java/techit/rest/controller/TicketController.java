package techit.rest.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import techit.model.Ticket;
import techit.model.Ticket.Priority;
import techit.model.Ticket.Status;
import techit.model.Unit;
import techit.model.Update;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.TicketDao;
import techit.model.dao.UnitDao;
import techit.model.dao.UpdateDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class TicketController {

	@Autowired
	private TicketDao ticketDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UnitDao unitDao;

	@Autowired
	private UpdateDao updateDao;

	private static Logger logger = LogManager.getLogger(UserController.class);

	// Get all tickets.
	@RequestMapping(value = "/tickets", method = RequestMethod.GET, produces = "application/json")
	public List<Ticket> getTickets(HttpServletRequest req) {
		logger.info("Inside getTickets");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());

		// Nobody other than Admin can get all the tickets.
		if (!isLoggedInUserAdmin) {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
		return ticketDao.getTickets();
	}

	// Get a ticket by id.
	@RequestMapping(value = "/tickets/{id}", method = RequestMethod.GET, produces = "application/json")
	public Ticket getTicket(@PathVariable Long id, HttpServletRequest req) {
		logger.info("Inside getTicket");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(id);
		if (ticket == null) {
			throw new RestException(404, "Ticket Not Found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserCreator = ticket.getCreatedBy().getId().toString()
				.equals(loggedInUser.get("id").toString());
		boolean isLoggedInUserSupervisor = false;
		boolean isLoggedInUserTechnician = false;

		List<User> supervisors = ticket.getUnit().getSupervisors();
		if (supervisors != null) {
			for (User supervisor : supervisors) {
				if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
					isLoggedInUserSupervisor = true;
					break;
				}
			}
		}

		List<User> techinicians = ticket.getTechnicians();
		if (techinicians != null) {
			for (User technician : techinicians) {
				if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
					isLoggedInUserTechnician = true;
					break;
				}
			}
		}

		// Only Admin + Creator of the ticket + Supervisors of Ticket's Unit +
		// Technicians of that ticket can get the ticket.
		if (isLoggedInUserAdmin || isLoggedInUserCreator || isLoggedInUserSupervisor || isLoggedInUserTechnician) {
			return ticket;
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

	// Create a new ticket.
	@RequestMapping(value = "/tickets", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public Ticket addTicket(@RequestBody Ticket ticket, HttpServletRequest req) {
		logger.info("Inside addTicket ");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		if (ticket == null) {
			throw new RestException(400, "Ticket is missing in the request");
		}

		// If ticket misses mandatory information then it cannot be created.
		if (ticket.getCreatedBy() == null || ticket.getCreatedForEmail() == null
				|| ticket.getCreatedForEmail().trim().equals("") || ticket.getSubject() == null
				|| ticket.getSubject().trim().equals("") || ticket.getUnit() == null) {
			throw new RestException(400,
					"CreatedBy and/or Email and/or Subject and/or Unit is/are missing in the request");
		}

		ticket.setDateCreated(new Date());
		ticket = ticketDao.saveTicket(ticket);
		return ticket;
	}

	// Get the tickets submitted by a user.
	@RequestMapping(value = "/users/{userId}/tickets", method = RequestMethod.GET, produces = "application/json")
	public List<Ticket> getTicketSubmittedByUser(@PathVariable Long userId, HttpServletRequest req) {
		logger.info("Inside getTicketSubmittedByUser ");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		User user = userDao.getUser(userId);
		if (user == null) {
			throw new RestException(404, "User not found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSelf = loggedInUser.get("id").toString().equals(userId.toString());

		// Only Admin + Creator of the ticket can get the ticket.
		if (isLoggedInUserAdmin || isLoggedInUserSelf) {
			return ticketDao.getTicketsCreatedBy(userDao.getUser(userId));
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}

	}

	// Get the tickets assigned to a technician.
	@RequestMapping(value = "/technicians/{userId}/tickets", method = RequestMethod.GET, produces = "application/json")
	public List<Ticket> getTicketsAssignedToTechnician(@PathVariable Long userId, HttpServletRequest req) {
		logger.info("Inside getTicketsAssignedToTechnician");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		User techinician = userDao.getUser(userId);

		if (techinician == null) {
			throw new RestException(404, "Technician not found.");
		}

		// if the passed userid in the path variable is not Technicians then throw an
		// error.
		if (!techinician.getType().equals(Type.TECHNICIAN)) {
			throw new RestException(400, "ID in request doesnot belong to technician.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSelf = loggedInUser.get("id").toString().equals(userId.toString());
		boolean isLoggedInUserSupervisor = false;

		List<User> supervisors = techinician.getUnit().getSupervisors();

		if (supervisors != null) {
			for (User supervisor : supervisors) {
				if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
					isLoggedInUserSupervisor = true;
					break;
				}
			}
		}

		// Only Admin + Supervisor of Ticket's Unit and the loggedin technician can get
		// the tickets.
		if (isLoggedInUserSupervisor || isLoggedInUserSelf || isLoggedInUserAdmin) {
			return ticketDao.getTicketsAssignedTo(techinician);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}

	}

	// Edit the ticket with the id (excluding updates).
	@RequestMapping(value = "/tickets/{ticketId}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public Ticket editTicket(@PathVariable Long ticketId, HttpServletRequest req, @RequestBody Ticket updatedTicket) {
		logger.info("Inside editTicket");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not found.");
		}

		// if ticket misses mandatory information then cannot be created.
		if (updatedTicket.getCreatedBy() == null || updatedTicket.getCreatedForEmail() == null
				|| updatedTicket.getCreatedForEmail().trim().equals("") || updatedTicket.getSubject() == null
				|| updatedTicket.getSubject().trim().equals("") || updatedTicket.getUnit() == null) {
			throw new RestException(400,
					"CreatedBy and/or Email and/or Subject and/or Unit is/are missing in the request");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserCreator = ticket.getCreatedBy().getId().toString()
				.equals(loggedInUser.get("id").toString());

		// Admin + Creator of the ticket + can edit the ticket.
		if (isLoggedInUserCreator || isLoggedInUserAdmin) {
			updatedTicket.setId(ticketId);
			updatedTicket.setDateUpdated(new Date());
			return ticketDao.saveTicket(updatedTicket);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}

	}

	// Assign a technician to a ticket.
	@RequestMapping(value = "/tickets/{ticketId}/technicians/{userId}", method = RequestMethod.PUT, produces = "application/json")
	public Ticket assignTechnicianToTicket(@PathVariable Long ticketId, @PathVariable Long userId,
			HttpServletRequest req) {
		logger.info("Inside assignTechnicianToTicket");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		User user = userDao.getUser(userId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not Found");
		}
		if (user == null) {
			throw new RestException(404, "User not Found");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isloggedInUserTechnician = false;
		boolean isloggedInUserSupervisor = false;

		// logged in technician who belongs to the ticket's unit can assign ticket to
		// him self.
		List<User> technicians = ticket.getUnit().getTechnicians();
		for (User technician : technicians) {
			if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserTechnician = true;
				break;
			}
		}

		// logged in supervisor who belong to the ticket's unit can assign ticket to his
		// technicians.
		List<User> supervisors = ticket.getUnit().getSupervisors();
		for (User supervisor : supervisors) {
			if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserSupervisor = true;
				break;
			}
		}

		// admin can also assign technician to a ticket.
		if (isloggedInUserSupervisor || isloggedInUserTechnician || isLoggedInUserAdmin) {
			List<User> users = new ArrayList<User>();
			users.add(user);
			ticket.setTechnicians(users);
			ticket.setStatus(Status.ASSIGNED);
			ticket.setDateAssigned(new Date());
			return ticketDao.saveTicket(ticket);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

	// Set the status of a ticket.
	@RequestMapping(value = "/tickets/{ticketId}/status/{status}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public Ticket updateStatus(@PathVariable Long ticketId, @PathVariable String status,
			@RequestBody(required = false) String message, HttpServletRequest req) {
		logger.info("Inside updateStatus");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isloggedInUserTechnician = false;
		boolean isloggedInUserSupervisor = false;

		List<User> technicians = ticket.getTechnicians();
		for (User technician : technicians) {
			if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserTechnician = true;
				break;
			}
		}
		List<User> supervisors = ticket.getUnit().getSupervisors();
		for (User supervisor : supervisors) {
			if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserSupervisor = true;
				break;
			}
		}

		// Only Admin + supervisor of the ticket's unit + technicians working on that
		// ticket can update the status
		if (isloggedInUserTechnician || isLoggedInUserAdmin || isloggedInUserSupervisor) {
			switch (status.toUpperCase()) {
			case "ASSIGNED":
				ticket.setStatus(Status.ASSIGNED);
				break;
			case "CLOSED":
				ticket.setStatus(Status.ClOSED);
				ticket.setDateClosed(new Date());
				break;
			case "COMPLETED": {
				ticket.setStatus(Status.COMPLETED);
				break;
			}
			case "ONHOLD":
				ticket.setStatus(Status.ONHOLD);
				break;
			case "OPEN":
				ticket.setStatus(Status.OPEN);
				break;
			default:
				throw new RestException(501, "Invalid Status of the Ticket");
			}
			ticket.setDateUpdated(new Date());
			Update update = new Update();
			if (message != null && !message.trim().equals("")) {
				update.setDetails(message);
			}

			update.setDate(new Date());
			update.setTicket(ticket);
			update.setTechnician(userDao.getUser(Long.parseLong(loggedInUser.get("id").toString())));
			update = updateDao.saveUpdate(update);
			List<Update> updates = new ArrayList<>();
			updates.add(update);
			ticket.setUpdates(updates);
			return ticketDao.saveTicket(ticket);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

	// Set the priority of a ticket.
	@RequestMapping(value = "/tickets/{ticketId}/priority/{priority}", method = RequestMethod.PUT, produces = "application/json")
	public Ticket setPriority(@PathVariable Long ticketId, @PathVariable String priority, HttpServletRequest req) {
		logger.info("Inside setPriority");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isloggedInUserTechnician = false;
		boolean isloggedInUserSupervisor = false;

		List<User> technicians = ticket.getTechnicians();
		for (User technician : technicians) {
			if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserTechnician = true;
				break;
			}
		}
		List<User> supervisors = ticket.getUnit().getSupervisors();
		for (User supervisor : supervisors) {
			if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserSupervisor = true;
				break;
			}
		}

		// Only Admin + supervisor of the ticket's unit + technicians working on that
		// ticket can update the priority
		if (isloggedInUserTechnician || isLoggedInUserAdmin || isloggedInUserSupervisor) {
			switch (priority.toUpperCase()) {
			case "LOW":
				ticket.setPriority(Priority.LOW);
				break;
			case "MEDIUM":
				ticket.setPriority(Priority.MEDIUM);
				break;
			case "HIGH":
				ticket.setPriority(Priority.HIGH);
				break;
			default:
				throw new RestException(501, "Invalid Priority of the Ticket");
			}
			return ticketDao.saveTicket(ticket);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

	// Add an update to a ticket
	@RequestMapping(value = "/tickets/{ticketId}/updates", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public Ticket updateTicket(@PathVariable Long ticketId, @RequestBody Update update, HttpServletRequest req) {
		logger.info("Inside updateTicket");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not found.");
		}

		if (update == null) {
			throw new RestException(404, "Update not found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isloggedInUserTechnician = false;
		boolean isloggedInUserSupervisor = false;

		List<User> technicians = ticket.getTechnicians();
		for (User technician : technicians) {
			if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserTechnician = true;
				break;
			}
		}
		List<User> supervisors = ticket.getUnit().getSupervisors();
		for (User supervisor : supervisors) {
			if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
				isloggedInUserSupervisor = true;
				break;
			}
		}

		// Only Admin + supervisor of the ticket's unit + technicians working on that
		// ticket can add updates to the ticket.
		if (isloggedInUserTechnician || isLoggedInUserAdmin || isloggedInUserSupervisor) {
			update.setTicket(ticket);
			update.setDate(new Date());
			update = updateDao.saveUpdate(update);
			List<Update> updates = new ArrayList<>();
			updates.add(update);
			ticket.setUpdates(updates);
			return ticket;
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

	// Get the tickets submitted to a unit.
	@RequestMapping(value = "/units/{unitId}/tickets", method = RequestMethod.GET, produces = "application/json")
	public List<Ticket> getTicketsOfUnit(@PathVariable Long unitId, HttpServletRequest req) {
		logger.info("Inside getTicketsOfUnit");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Unit unit = unitDao.getUnit(unitId);
		if (unit == null) {
			throw new RestException(400, "No Unit found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSupervisor = false;
		boolean isLoggedInUserTechnician = false;

		List<User> supervisors = unit.getSupervisors();
		for (User supervisor : supervisors) {
			if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
				isLoggedInUserSupervisor = true;
				break;
			}
		}

		List<User> technicians = unit.getTechnicians();
		for (User technician : technicians) {
			if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
				isLoggedInUserTechnician = true;
				break;
			}
		}

		// Only Admin + supervisor of the unit + technicians of unit can get the
		// tickets.
		if (isLoggedInUserAdmin || isLoggedInUserSupervisor || isLoggedInUserTechnician) {
			return ticketDao.getTicketsAssignedTo(unitDao.getUnit(unitId));
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}
}
