package third.task.stripe.dto;

public class CustomerDto {
    private Long id;
    private String StripeId;
    private String email;

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

    @Override
    public String toString() {
        return "CustomerDto{" +
                "id=" + id +
                ", StripeId='" + StripeId + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
