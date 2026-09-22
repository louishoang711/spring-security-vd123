package vn.iotstar;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
@SpringBootTest(properties = {"app.example=2","spring.datasource.url=jdbc:h2:mem:example2"}) class Example2IntegrationTest {
    @Autowired WebApplicationContext context; MockMvc mvc;
    @BeforeEach void setup() { mvc=MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build(); }
    @Test void usernameAndEmailBothWork() throws Exception {
        var result=mvc.perform(post("/login").with(csrf()).param("username","user01").param("password","ChangeMe123!"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/")).andReturn();
        mvc.perform(get("/").session((org.springframework.mock.web.MockHttpSession)result.getRequest().getSession()))
                .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Demo User")));
        mvc.perform(post("/login").with(csrf()).param("username","user@example.com").param("password","ChangeMe123!"))
                .andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/"));
    }
}
