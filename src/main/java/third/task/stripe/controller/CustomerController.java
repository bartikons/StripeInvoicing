package third.task.stripe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import third.task.stripe.command.LoginCommand;
import third.task.stripe.dto.JwtDto;
import third.task.stripe.service.CustomerService;

@Controller
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService=customerService;
    }

    @PostMapping("/logIn")
    public ResponseEntity<JwtDto> LogInUser(@RequestBody LoginCommand loginCommand) {
        return customerService.logIn(loginCommand);
    }

    @PostMapping("/CreateAccount")
    public ResponseEntity<String> createAccount(@RequestBody LoginCommand loginCommand) {
        return customerService.createAccount(loginCommand);
    }
}
