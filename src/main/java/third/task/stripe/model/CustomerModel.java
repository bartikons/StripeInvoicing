package third.task.stripe.model;

import javax.persistence.*;

@Entity
@Table(name = "customer_model")
public class CustomerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "stripe_id")
    private String StripeId;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String Password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStripeId() {
        return StripeId;
    }

    public void setStripeId(String stripeId) {
        StripeId = stripeId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "id=" + id  +
                ", email='" + email + '\'' +
                '}';
    }
}
