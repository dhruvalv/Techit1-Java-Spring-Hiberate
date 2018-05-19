package techit.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import techit.model.Unit;

@Test
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
public class TicketControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void getTickets_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "ADMIN");
		this.mockMvc.perform(get("/tickets").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTickets_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/tickets").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void addTicket_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("username", "dhruvalv");
		Unit unit = new Unit();
		unit.setId(1L);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/tickets")
				.contentType(MediaType.APPLICATION_JSON).content(addTicketSuccessdata());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void addTicket_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("username", "dhruvalv");
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/tickets")
				.contentType(MediaType.APPLICATION_JSON).content(addTicketFailuredata());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isBadRequest());
	}

	@Test
	void getTicketById_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 8);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/tickets/1").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTicketById_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 9);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/tickets/1").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	
	@Test
	void getTicketSubmittedByUser_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 8);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/users/8/tickets").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTicketSubmittedByUser_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 9);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/users/8/tickets").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void getTicketsAssignedToTechnician_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 4);
		map.put("type", "TECHNICIAN");
		this.mockMvc.perform(get("/technicians/4/tickets").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTicketsAssignedToTechnician_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 5);
		map.put("type", "TECHNICIAN");
		this.mockMvc.perform(get("/technicians/4/tickets").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void getTicketOfUnit_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 2);
		map.put("type", "SUPERVISOR");
		this.mockMvc.perform(get("/units/1/tickets").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTicketOfUnit_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 3);
		map.put("type", "SUPERVISOR");
		this.mockMvc.perform(get("/units/1/tickets").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void editTicket_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("id", 8);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1")
				.contentType(MediaType.APPLICATION_JSON).content(editTicketData());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void editTicket_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("id", 9);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1")
				.contentType(MediaType.APPLICATION_JSON).content(editTicketData());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void assignTechnicianToTicket_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "SUPERVISOR");
		map.put("id", 2);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/technicians/2");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void assignTechnicianToTicket_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "SUPERVISOR");
		map.put("id", 3);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/technicians/2");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void updateStatus_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 4);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/status/onhold")
				.contentType(MediaType.APPLICATION_JSON).content("\"Ticket on Hold due to User unavailability\"");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void updateStatus_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 6);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/status/onhold")
				.contentType(MediaType.APPLICATION_JSON).content("\"Ticket on Hold due to User unavailability\"");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void setPriority_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 4);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/priority/high");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void setPriority_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 6);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/tickets/1/priority/high");
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void addUpdateToTicket_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 4);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/tickets/1/updates")
				.contentType(MediaType.APPLICATION_JSON).content(addUpdateData());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void addUpdateToTicket_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "TECHNICIAN");
		map.put("id", 6);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/tickets/1/updates")
				.contentType(MediaType.APPLICATION_JSON).content(addUpdateData());
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	private static String addTicketSuccessdata() {
		return "{" + "\"createdBy\":" + "{" + "\"id\":" + 8 + "," + "\"type\": \"REGULAR\","
				+ "\"username\": \"dhruvalv\"," + "\"enabled\":" + true + "," + "\"firstName\": \"Dhruval\","
				+ "\"lastName\": \"Variya\"," + "\"email\": \"dhruvalv@gmai;.com\"," + "\"phone\":" + null + ","
				+ "\"department\":" + null + "," + "\"unitId\":" + null + "},"
				+ "\"createdForName\": \"Dhruval Variya\"," + "\"createdForEmail\": \"dhruvalv@gmai;.com\","
				+ "\"createdForPhone\":" + null + "," + "\"createdForDepartment\":" + null + ","
				+ "\"subject\": \"xxxxxxxxxxxx\"," + "\"details\": \"xxxxxxxx xxxxxxxxx xxxxxxxxxxx.\","
				+ "\"location\":" + null + "," + "\"unit\": {" + "\"id\":" + 1 + "}," + "\"technicians\": [],"
				+ "\"updates\": []," + "\"priority\": \"MEDIUM\"," + "\"status\": \"OPEN\"," + "\"dateCreated\":" + null
				+ "," + "\"dateAssigned\":" + null + "," + "\"dateUpdated\":" + null + "," + "\"dateClosed\":" + null
				+ "}";
	}

	private static String addTicketFailuredata() {
		return "{" + "\"createdBy\":" + "{" + "\"id\":" + 8 + "," + "\"type\": \"REGULAR\","
				+ "\"username\": \"dhruvalv\"," + "\"enabled\":" + true + "," + "\"firstName\": \"Dhruval\","
				+ "\"lastName\": \"Variya\"," + "\"email\": \"dhruvalv@gmai;.com\"," + "\"phone\":" + null + ","
				+ "\"department\":" + null + "," + "\"unitId\":" + null + "},"
				+ "\"createdForName\": \"Dhruval Variya\"," + "\"createdForEmail\": " + null + ","
				+ "\"createdForPhone\":" + null + "," + "\"createdForDepartment\":" + null + ","
				+ "\"subject\": \"xxxxxxxxxxxx\"," + "\"details\": \"xxxxxxxx xxxxxxxxx xxxxxxxxxxx.\","
				+ "\"location\":" + null + "," + "\"unit\": {" + "\"id\":" + 1 + "}," + "\"technicians\": [],"
				+ "\"updates\": []," + "\"priority\": \"MEDIUM\"," + "\"status\": \"OPEN\"," + "\"dateCreated\":" + null
				+ "," + "\"dateAssigned\":" + null + "," + "\"dateUpdated\":" + null + "," + "\"dateClosed\":" + null
				+ "}";
	}

	private static String editTicketData() {
		return "{" + "\"createdBy\":" + "{" + "\"id\":" + 8 + "," + "\"type\": \"REGULAR\","
				+ "\"username\": \"dhruvalv\"," + "\"enabled\":" + true + "," + "\"firstName\": \"Dhruval\","
				+ "\"lastName\": \"Variya\"," + "\"email\": \"dhruvalv@gmai;.com\"," + "\"phone\":" + null + ","
				+ "\"department\":" + null + "," + "\"unitId\":" + null + "},"
				+ "\"createdForName\": \"Dhruval Variya\"," + "\"createdForEmail\": \"dhruvalv@gmai;.com\","
				+ "\"createdForPhone\":" + null + "," + "\"createdForDepartment\":" + null + ","
				+ "\"subject\": \"xxxxxxxxxxxx\"," + "\"details\": \"yyyyy yyyyy yyyyyyyyyyyyy.\"," + "\"location\":"
				+ null + "," + "\"unit\": {" + "\"id\":" + 1 + "}," + "\"technicians\": []," + "\"updates\": [],"
				+ "\"priority\": \"MEDIUM\"," + "\"status\": \"OPEN\"," + "\"dateCreated\":" + null + ","
				+ "\"dateAssigned\":" + null + "," + "\"dateUpdated\":" + null + "," + "\"dateClosed\":" + null + "}";
	}

	private static String addUpdateData() {
		return "{" + " \"technician\": {" + "     \"id\":" + 4 + "},"
				+ " \"details\": \"Wrong ticket, need to raise to another dept.\"" + "}";
	}

}
