package se.maokei.event_jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import se.maokei.event_jwt.config.SecurityConfiguration;
import se.maokei.event_jwt.controller.AuthController;
import se.maokei.event_jwt.controller.EventController;
import se.maokei.event_jwt.security.JwtTokenProvider;
import se.maokei.event_jwt.service.UserDetailsServiceImpl;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AuthController.class, EventController.class})
@Import({SecurityConfiguration.class, JwtTokenProvider.class, UserDetailsServiceImpl.class})
public class ControllerTest {
    @Autowired
    MockMvc mockMvc;

    public void performLogin() throws Exception {

        var res = this.mockMvc.perform(post("/api/login")
                .with(httpBasic("admin@gmail.com","password")))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getEvents() throws Exception {
        var res = this.mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getSpecialWithoutLogin() throws Exception {
        var res = this.mockMvc.perform(get("/api/special"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }
}
