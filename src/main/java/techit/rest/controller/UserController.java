package techit.rest.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.User.Type;
import techit.model.dao.TicketDao;
import techit.model.dao.UnitDao;
import techit.model.dao.UserDao;
import techit.rest.error.RestException;

@RestController
public class UserController {

	@Autowired
	private UserDao userDao;

	@Autowired
	private UnitDao unitDao;

	@Autowired
	private TicketDao ticketDao;

	private static Logger logger = LogManager.getLogger(UserController.class);
	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public String login(@RequestBody User users) {
		logger.info("Inside login " + users.toString());
		String jwtToken = "";
		User user = userDao.getUser(users.getUsername());
		boolean flag = passwordEncoder.matches(users.getPassword(), user.getHash());

		if (!flag || null == user) {
			throw new RestException(500, "Invalid username and/or password.");
		}

		jwtToken = Jwts.builder().claim("user", user).setIssuedAt(new Date())
				.signWith(SignatureAlgorithm.HS256, "dhruval").compact();

		return jwtToken;
	}

	// Get all users.
	@RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
	public List<User> getAllUsers(HttpServletRequest req) {
		logger.info("Inside getAllUsers");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);
		
		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());

		// Nobody other than Admin can get all the users details.
		if (!isLoggedInUserAdmin) {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
		return userDao.getUsers();
	}

	// Create a new user.
	@RequestMapping(value = "/users", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public User addUser(@RequestBody User user, HttpServletRequest req) {
		logger.info("Inside addUser");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		// Nobody other than Admin can add a user.
		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		if (!isLoggedInUserAdmin) {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}

		// if Mandatory information is missing from the user then, it can be created.
		if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
			throw new RestException(400, "Missing username and/or password and/or email.");
		}

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		user.setHash(passwordEncoder.encode(user.getPassword()));

		return userDao.saveUser(user);
	}

	// Get a user by id.
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET, produces = "application/json")
	public User getUser(@PathVariable Long id, HttpServletRequest req) {
		logger.info("Inside getUser");
		User user = userDao.getUser(id);
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSelf = loggedInUser.get("id").toString().equals(id.toString());

		if (user == null) {
			throw new RestException(404, "User Not Found.");
		}

		// Only Admin and LoggedInUser himslef can get his/her details
		if (isLoggedInUserAdmin || isLoggedInUserSelf) {
			user = userDao.getUser(id);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
		return user;
	}

	// Edit the user with the id (excluding tickets).
	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
	public User editUser(@PathVariable Long id, @RequestBody User updatedUser, HttpServletRequest req) {
		logger.info("Inside editUser");
		User user = userDao.getUser(id);
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		if (user == null) {
			throw new RestException(404, "User Not Found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSelf = loggedInUser.get("id").toString().equals(id.toString());

		// Only Admin and LoggedInUser himslef can edit his/her details
		if (isLoggedInUserSelf || isLoggedInUserAdmin) {
			if (updatedUser == null) {
				throw new RestException(501, "User missing.");
			}
			// if mandatory details are missing then throw and error
			if (updatedUser.getUsername() == null || updatedUser.getUsername().trim().equals("")
					|| updatedUser.getPassword() == null || updatedUser.getPassword().trim().equals("")
					|| updatedUser.getEmail() == null || updatedUser.getEmail().trim().equals("")) {
				throw new RestException(400, "Missing username and/or password and/or email.");
			}
			updatedUser.setId(id);
			updatedUser.setHash(passwordEncoder.encode(updatedUser.getPassword()));
			updatedUser = userDao.saveUser(updatedUser);
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
		return updatedUser;
	}

	// Get the technicians of a unit.
	@RequestMapping(value = "/units/{unitId}/technicians", method = RequestMethod.GET, produces = "application/json")
	public List<User> getTechiniciansOfUnit(@PathVariable Long unitId, HttpServletRequest req) {
		logger.info("Inside getTechiniciansOfUnit");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		List<User> technicians = null;
		Unit unit = unitDao.getUnit(unitId);
		
		if (unit == null) {
			throw new RestException(404, "Unit Not Found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserSupervisor = false;
		boolean isLoggedInUserTechnician = false;

		List<User> supervisors = unit.getSupervisors();
		if (supervisors != null) {
			for (User supervisor : supervisors) {
				if (supervisor.getId().toString().equals(loggedInUser.get("id").toString())) {
					isLoggedInUserSupervisor = true;
					break;
				}
			}
		}

		List<User> techinicians = unit.getTechnicians();
		if (techinicians != null) {
			for (User technician : techinicians) {
				if (technician.getId().toString().equals(loggedInUser.get("id").toString())) {
					isLoggedInUserTechnician = true;
					break;
				}
			}
		}

		// Only Admin + Unit's supervisor + Unit's technicians can get the technicians.
		if (isLoggedInUserAdmin || isLoggedInUserSupervisor || isLoggedInUserTechnician) {
			//technicians = userDao.getTechniciansOfUnit(unit);
			technicians = unit.getTechnicians();
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}

		return technicians;
	};

	// Get the technicians assigned to a ticket.
	@RequestMapping(value = "/tickets/{ticketId}/technicians", method = RequestMethod.GET, produces = "application/json")
	public List<User> getTechiniciansOfTicket(@PathVariable Long ticketId, HttpServletRequest req) {
		logger.info("Inside getTechiniciansOfTicket");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);

		Ticket ticket = ticketDao.getTicket(ticketId);
		if (ticket == null) {
			throw new RestException(404, "Ticket not Found.");
		}

		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		boolean isLoggedInUserCreator = ticket.getCreatedBy().getId().toString()
				.equals(loggedInUser.get("id").toString());
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

		// Only Admim + creator + ticket's technicians + ticket's unit's supervisors can get the technicians working that a ticket.
		if (isLoggedInUserCreator || isloggedInUserSupervisor || isloggedInUserTechnician || isLoggedInUserAdmin) {
			return ticket.getTechnicians();
		} else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

}
