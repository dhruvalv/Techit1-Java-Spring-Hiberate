package techit.rest.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
//@Rollback(false)
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
public class UserControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void login_successful() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON).content(loginData("techit", "abcd"));

		this.mockMvc.perform(builder).andExpect(status().isOk());
	}

	@Test
	void login_failure() throws Exception {
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/login")
				.contentType(MediaType.APPLICATION_JSON).content(loginData("techit", "abcde"));
		this.mockMvc.perform(builder).andExpect(status().isInternalServerError());
	}

	@Test
	void getAllUsers_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "ADMIN");
		this.mockMvc.perform(get("/users").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getAllUsers_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/users").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void addUser_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "ADMIN");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON).content(addUserdata("REGULAR","shrey","shrey",true,"Shrey","Tarsaria","star@gmail.com",null,null,null));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void addUser_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON).content(addUserdata("REGULAR","shrey1","shrey1",true,"Shrey1","Tarsaria1","star@gmail.com1",null,null,null));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void getUserById_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 8);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/users/8").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getUserById_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 8);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/users/7").requestAttr("user", map)).andExpect(status().isForbidden());
	}
	
	@Test
	void editUser_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("id", 8);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/users/8")
				.contentType(MediaType.APPLICATION_JSON).content(editUserdata("REGULAR","dhruvalv","abcd",true,"Dhruval","Variya","dhruvalv@gmai;.com",null,"CS",null));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}
	
	@Test
	void editUser_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		map.put("id", 8);
		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.put("/users/4")
				.contentType(MediaType.APPLICATION_JSON).content(editUserdata("REGULAR","dhruvalv","abcd",true,"Dhruval","Variya","dhruvalv@gmai;.com",null,"CS",null));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}
	
	
	@Test
	void getTechnicianOfUnit_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 2);
		map.put("type", "SUPERVISOR");
		this.mockMvc.perform(get("/units/1/technicians").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTechnicianOfUnit_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 8);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/units/1/technicians").requestAttr("user", map)).andExpect(status().isForbidden());
	}
	
	@Test
	void getTechnicianOfTicket_Success() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 2);
		map.put("type", "SUPERVISOR");
		this.mockMvc.perform(get("/tickets/1/technicians").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getTechnicianOfTicket_Failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("id", 9);
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/tickets/1/technicians").requestAttr("user", map)).andExpect(status().isForbidden());
	}
	
	private static String loginData(String username, String password) {
		return "{ \"username\": \"" + username + "\", " + "\"password\":\"" + password + "\"}";
	}

	private static String addUserdata(String type, String username, String password, boolean enabled,
			String firstName, String lastName, String email, String phone, String department, Unit unit) {
		return "{ " + "\"type\":\"" + type + "\"," + "\"username\":\"" + username + "\","+ "\"password\":\"" + password + "\","
				+ "\"enabled\":" + enabled + "," + "\"firstName\":\"" + firstName + "\"," + "\"lastName\":\""
				+ lastName + "\"," + "\"email\":\"" + email + "\"," + "\"phone\":\"" + phone + "\","
				+ "\"department\":\"" + department + "\"," + "\"unitId\":\"" + unit + "\"}";
	}
	
	private static String editUserdata(String type, String username, String password, boolean enabled,
			String firstName, String lastName, String email, String phone, String department, Unit unit) {
		return "{ " + "\"type\":\"" + type + "\"," + "\"username\":\"" + username + "\","+ "\"password\":\"" + password + "\","
				+ "\"enabled\":" + enabled + "," + "\"firstName\":\"" + firstName + "\"," + "\"lastName\":\""
				+ lastName + "\"," + "\"email\":\"" + email + "\"," + "\"phone\":\"" + phone + "\","
				+ "\"department\":\"" + department + "\"," + "\"unitId\":\"" + unit + "\"}";
	}
}
