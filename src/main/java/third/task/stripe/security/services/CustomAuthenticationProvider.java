package third.task.stripe.security.services;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Component;
import third.task.stripe.model.CustomerModel;
import third.task.stripe.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomerRepository customerRepository;

    public CustomAuthenticationProvider(CustomerRepository customerRepository) {
        super();
        this.customerRepository = customerRepository;
    }


    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String user = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<CustomerModel> optionalCustomerModel = customerRepository.findByEmail(user);
        List<SimpleGrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("Customer"));
        if (optionalCustomerModel.isEmpty()) {
            throw new UsernameNotFoundException("Customer not found");
        }
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder();
        if (!encoder.matches(password, optionalCustomerModel.get().getPassword())) {
            throw new BadCredentialsException("incorrect password");
        }

        try {
            return new UsernamePasswordAuthenticationToken(user, password,authorityList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
                UsernamePasswordAuthenticationToken.class);
    }
}
