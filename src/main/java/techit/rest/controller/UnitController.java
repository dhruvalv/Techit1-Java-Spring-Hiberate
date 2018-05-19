package techit.rest.controller;

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

import techit.model.Unit;
import techit.model.User.Type;
import techit.model.dao.UnitDao;
import techit.rest.error.RestException;

@RestController
public class UnitController {

	@Autowired
	private UnitDao unitDao;

	private static Logger logger = LogManager.getLogger(UserController.class);

	// Get all units. 
	@RequestMapping(value = "/units", method = RequestMethod.GET, produces = "application/json")
	public List<Unit> getUnits(HttpServletRequest req) {
		logger.info("Inside getTickets");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);
		
		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		
		// Nobody other than Admin can get all the unit details.
		if (!isLoggedInUserAdmin) {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
		return unitDao.getUnits();
	}

	@RequestMapping(value = "/units/{id}", method = RequestMethod.GET, produces = "application/json")
	public Unit getUnit(@PathVariable Long id, HttpServletRequest req) {
		logger.info("Inside getTickets");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);
		return unitDao.getUnit(id);
	}

	// Create a new unit.
	@RequestMapping(value = "/units", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public Unit addUnit(@RequestBody Unit unit, HttpServletRequest req) {
		logger.info("Inside addUnit");
		Map loggedInUser = (Map) req.getAttribute("user");
		logger.debug("loggedInUser = " + loggedInUser);
		
		if(unit == null) {
			throw new RestException(400, "Unit not present in the request body");
		}
		if(unit.getName() == null) {
			throw new RestException(400, "Unit name is missing");
		}
		
		// Only Admin can create a new Unit
		boolean isLoggedInUserAdmin = loggedInUser.get("type").equals(Type.ADMIN.toString());
		if(isLoggedInUserAdmin) {
			return unitDao.saveUnit(unit);
		}else {
			throw new RestException(403, "Forbidden, not allowed to access.");
		}
	}

}
