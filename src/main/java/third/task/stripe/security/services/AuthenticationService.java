package third.task.stripe.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import third.task.stripe.command.JwtCommand;
import third.task.stripe.command.LoginCommand;
import third.task.stripe.repository.CustomerRepository;
import third.task.stripe.security.jwt.JwtProvider;


@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtProvider jwtProvider, CustomerRepository customerRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
    }

    public JwtCommand auth(LoginCommand loginCommand) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginCommand.getEmail(), loginCommand.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwtToken(authentication);
        return new JwtCommand(jwt);
    }


    public Long getIdFromJwt() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserPrinciple) principal).getId();
        } else {
            return -1L;
        }
    }

}
