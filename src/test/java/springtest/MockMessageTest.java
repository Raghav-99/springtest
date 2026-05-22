package springtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(value = Lifecycle.PER_CLASS)
public class MockMessageTest {
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeAll
	void generateMessage() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/message").content("hello!"))
		.andExpect(MockMvcResultMatchers.status().isCreated());
	}
	
	@Test
	public void getNonEmptyMessage() throws Exception {
		
		MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/message")).andReturn();
		MessageResponse expected = new MessageResponse(new MessageEntity(1, "hello!", MessageStatus.PENDING.name()), null);
		assertEquals(expected.toString(), result.getResponse().getContentAsString());
	}
}
