package third.task.stripe.model;

import javax.persistence.*;

@Entity
@Table(name = "invoice")
public class InvoiceModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "stripe_Id")
    private String stripeId;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "customer_model", referencedColumnName = "id")
    private CustomerModel customerModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return stripeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public CustomerModel getCustomerModel() {
        return customerModel;
    }

    public void setCustomerModel(CustomerModel customerModel) {
        this.customerModel = customerModel;
    }

    @Override
    public String toString() {
        return "InvoiceModel{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", customerModel=" + customerModel +
                '}';
    }
}
