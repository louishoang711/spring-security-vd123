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

@SpringBootTest(properties = "app.example=3") class Example3IntegrationTest {
    @Autowired WebApplicationContext context;
    MockMvc mvc;
    @BeforeEach void setup() { mvc=MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity()).build(); }
    @Test void anonymousCannotAccessProducts() throws Exception {
        mvc.perform(get("/products")).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
    }
    @Test void userCanLoginButCannotManageUsers() throws Exception {
        var login=mvc.perform(post("/login").with(csrf()).param("username","user01").param("password","ChangeMe123!"))
                .andExpect(status().is3xxRedirection()).andReturn();
        mvc.perform(get("/products").session((org.springframework.mock.web.MockHttpSession)login.getRequest().getSession()))
                .andExpect(status().isOk());
        mvc.perform(get("/users").session((org.springframework.mock.web.MockHttpSession)login.getRequest().getSession()))
                .andExpect(status().isForbidden());
    }
    @Test void adminCanManageUsers() throws Exception {
        var login=mvc.perform(post("/login").with(csrf()).param("username","admin@example.com").param("password","ChangeMe123!"))
                .andExpect(status().is3xxRedirection()).andReturn();
        mvc.perform(get("/users").session((org.springframework.mock.web.MockHttpSession)login.getRequest().getSession()))
                .andExpect(status().isOk()).andExpect(view().name("users/list"));
        mvc.perform(get("/users/new").session((org.springframework.mock.web.MockHttpSession)login.getRequest().getSession()))
                .andExpect(status().isOk());
        mvc.perform(get("/products/new").session((org.springframework.mock.web.MockHttpSession)login.getRequest().getSession()))
                .andExpect(status().isOk());
    }
    @Test void registrationCreatesDisabledUserAndRejectsWrongOtp() throws Exception {
        mvc.perform(post("/register").with(csrf()).param("username","newstudent")
                .param("email","newstudent@example.com").param("fullName","New Student")
                .param("password","Student123!").param("confirmPassword","Student123!"))
                .andExpect(status().is3xxRedirection());
        mvc.perform(post("/login").with(csrf()).param("username","newstudent")
                .param("password","Student123!"))
                .andExpect(redirectedUrl("/login?error"));
        mvc.perform(post("/verify-otp").with(csrf()).param("email","newstudent@example.com")
                .param("code","000000")).andExpect(status().isOk());
    }
    @Test void userCanCreateAndSearchOwnProduct() throws Exception {
        var login=mvc.perform(post("/login").with(csrf()).param("username","user01")
                .param("password","ChangeMe123!")).andExpect(status().is3xxRedirection()).andReturn();
        var session=(org.springframework.mock.web.MockHttpSession)login.getRequest().getSession();
        mvc.perform(post("/products/save").session(session).with(csrf()).param("name","Notebook Sample")
                .param("description","Test product").param("price","125.50"))
                .andExpect(redirectedUrl("/products"));
        mvc.perform(get("/products").session(session).param("keyword","Notebook"))
                .andExpect(status().isOk()).andExpect(content().string(org.hamcrest.Matchers.containsString("Notebook Sample")));
    }
}
