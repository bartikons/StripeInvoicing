package third.task.stripe.service;

import com.stripe.Stripe;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import third.task.stripe.security.services.AuthenticationService;
import third.task.stripe.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import third.task.stripe.command.LoginCommand;
import third.task.stripe.model.CustomerModel;
import third.task.stripe.command.JwtCommand;
import org.springframework.http.HttpStatus;
import third.task.stripe.dto.JwtDto;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final AuthenticationService authenticationService;
    private static final String regex = "^(.+)@(.+)$";
    @Value("${Stripe.apiKey}")
    private String StripeKey;

    public CustomerService(CustomerRepository customerRepository, AuthenticationService authenticationService) {
        this.customerRepository = customerRepository;
        this.authenticationService = authenticationService;
    }

    public ResponseEntity<String> createAccount(LoginCommand loginCommand) {
        if (customerRepository.findByEmail(loginCommand.getEmail()).isPresent()) {
            return new ResponseEntity<>("User with this username have been found", HttpStatus.FOUND);
        }
        CustomerModel customer = new CustomerModel();

        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(loginCommand.getEmail()).matches()) {
            return new ResponseEntity<>("Not Acceptable email format", HttpStatus.NOT_ACCEPTABLE);
        }
        customer.setEmail(loginCommand.getEmail());
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        customer.setPassword(encoder.encode(loginCommand.getPassword()));
        customer = customerRepository.save(customer);
        Map<String, Object> params = new HashMap<>();
        Map<String, String> initialMetadata = new HashMap<>();
        initialMetadata.put("db_id", customer.getId().toString());
        params.put("metadata", initialMetadata);
        try {
            Stripe.apiKey = StripeKey;
            Customer customerStripe = Customer.create(params);
            customer.setStripeId(customerStripe.getId());
            customerRepository.save(customer);
        } catch (Exception e) {
            customerRepository.delete(customer);
            e.printStackTrace();
            return new ResponseEntity<>("Please try later", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    public ResponseEntity<JwtDto> logIn(LoginCommand loginCommand) {
        JwtCommand jwtCommand = authenticationService.auth(loginCommand);
        return new ResponseEntity<>(parseJwtCommandToDto(jwtCommand), HttpStatus.OK);
    }

    private JwtDto parseJwtCommandToDto(JwtCommand jwtCommand) {
        return new JwtDto(jwtCommand.getToken(), jwtCommand.getType());
    }
}
