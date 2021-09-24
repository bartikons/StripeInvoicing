package third.task.stripe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import third.task.stripe.model.InvoiceModel;

import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<InvoiceModel, Long> {
    Optional<InvoiceModel> findByStripeId(String StripeId);
    Optional<InvoiceModel> findByStripeIdAndStatus(String StripeId, String Status);
    Optional<InvoiceModel> findById(Long id);
}