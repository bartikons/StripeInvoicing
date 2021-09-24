package third.task.stripe.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import third.task.stripe.security.services.UserPrinciple;
import third.task.stripe.model.CustomerModel;
import third.task.stripe.repository.CustomerRepository;

import javax.transaction.Transactional;

@Service
public class CustomerDetailServiceImpl implements UserDetailsService {

    final CustomerRepository customerRepository;

    public CustomerDetailServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public UserPrinciple loadUserByUsername(String username) throws UsernameNotFoundException {
        CustomerModel customer = customerRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with -> username " + username));
        return UserPrinciple.build(customer);

    }
}