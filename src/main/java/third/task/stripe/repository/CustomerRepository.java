package third.task.stripe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import third.task.stripe.model.CustomerModel;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel,Long> {

    Optional<CustomerModel> findById(Long id);
    Optional<CustomerModel> findByEmail(String email);
}