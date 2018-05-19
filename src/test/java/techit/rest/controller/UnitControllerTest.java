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
// @Rollback(false)
@ContextConfiguration(locations = { "classpath:techit-servlet.xml", "classpath:applicationContext.xml" })
public class UnitControllerTest extends AbstractTransactionalTestNGSpringContextTests {

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@BeforeClass
	void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	void getAllUnits_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "ADMIN");
		this.mockMvc.perform(get("/units").requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void getAllUnits_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");
		this.mockMvc.perform(get("/units").requestAttr("user", map)).andExpect(status().isForbidden());
	}

	@Test
	void addUnit_successful() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "ADMIN");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/units")
				.contentType(MediaType.APPLICATION_JSON).content(addUnitdata("techi1234"));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isOk());
	}

	@Test
	void addUnit_failure() throws Exception {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("type", "REGULAR");

		MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/units")
				.contentType(MediaType.APPLICATION_JSON).content(addUnitdata("techi123"));
		this.mockMvc.perform((builder).requestAttr("user", map)).andExpect(status().isForbidden());
	}

	private static String addUnitdata(String name) {
		return "{" + " \"name\":\"" + name + "\"," + " \"location\":" + null + "," + " \"email\":" + null + ","
				+ " \"phone\":" + null + "," + " \"description\":" + null + "," + " \"supervisors\":[],"
				+ " \"technicians\":[]" + "}";
	}

}
