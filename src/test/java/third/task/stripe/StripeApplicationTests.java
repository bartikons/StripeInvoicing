package third.task.stripe;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;
import third.task.stripe.command.LoginCommand;
import third.task.stripe.dto.JwtDto;
import third.task.stripe.repository.CustomerRepository;
import third.task.stripe.repository.InvoiceRepository;
import third.task.stripe.service.CustomerService;
import third.task.stripe.service.InvoiceService;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StripeApplicationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private InvoiceService invoiceService;

    @Test
    void AccountCreation() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        LoginCommand loginCommand = new LoginCommand("test@example.com.pl", "t3Stpassword");

        try {
            mockMvc.perform(post("/api/customer/CreateAccount")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand)))
                    .andExpect(status().isOk());
            customerRepository.delete(customerRepository.findByEmail(loginCommand.getEmail()).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void AccountDuplicateCreation() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");

        try {
            mockMvc.perform(post("/api/customer/CreateAccount")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand)))
                    .andExpect(status().isFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void LoginIn() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");

        try {
            mockMvc.perform(post("/api/customer/logIn")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand)))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void FailLoginIn() {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        LoginCommand loginCommand = new LoginCommand("naeris.bt@g=il.com", "passworda");
        LoginCommand loginCommand2 = new LoginCommand("naeris.b.com", "passworda");
        LoginCommand loginCommand3 = new LoginCommand("naeris.bt@gmail.com", "passda");
        LoginCommand loginCommand4 = new LoginCommand("", "");

        try {
            mockMvc.perform(post("/api/customer/logIn")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand)))
                    .andExpect(status().isUnauthorized());
            mockMvc.perform(post("/api/customer/logIn")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand2)))
                    .andExpect(status().isUnauthorized());
            mockMvc.perform(post("/api/customer/logIn")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand3)))
                    .andExpect(status().isUnauthorized());
            mockMvc.perform(post("/api/customer/logIn")
                            .contentType("application/json")
                            .content(ow.writeValueAsString(loginCommand4)))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void UnauthorizedAction() {
        try {
            mockMvc.perform(get("/api")
                            .contentType("application/json"))
                    .andExpect(status().isUnauthorized());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void AuthorizedAction() {
        try {
            LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");
            JwtDto jwtDto = customerService.logIn(loginCommand).getBody();
            mockMvc.perform(post("/api")
                            .contentType("application/json")
                            .header("Authorization", jwtDto.getType() + " " + jwtDto.getToken()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void BadRequestGetInvoice() {
        try {
            LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");
            JwtDto jwtDto = customerService.logIn(loginCommand).getBody();
            mockMvc.perform(get("/api")
                            .contentType("application/json")
                            .header("Authorization", jwtDto.getType() + " " + jwtDto.getToken()))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void GoodRequestGetInvoice() {
        try {
            LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");
            JwtDto jwtDto = customerService.logIn(loginCommand).getBody();
            mockMvc.perform(get("/api")
                            .contentType("application/json")
                            .header("Authorization", jwtDto.getType() + " " + jwtDto.getToken())
                            .param("invoiceId","1"))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void NotFoundGetInvoice() {
        try {
            LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");
            JwtDto jwtDto = customerService.logIn(loginCommand).getBody();
            mockMvc.perform(get("/api")
                            .contentType("application/json")
                            .header("Authorization", jwtDto.getType() + " " + jwtDto.getToken())
                            .param("invoiceId", "-1"))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void GetListOfInvoice() {
        try {
            LoginCommand loginCommand = new LoginCommand("naeris.bt@gmail.com", "passworda");
            JwtDto jwtDto = customerService.logIn(loginCommand).getBody();
            mockMvc.perform(get("/api/gets")
                            .contentType("application/json")
                            .header("Authorization", jwtDto.getType() + " " + jwtDto.getToken()))
                    .andExpect(status().isOk());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
