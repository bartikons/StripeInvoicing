package third.task.stripe.dto;

public class InvoiceDto {
    private Long id;
    private String StripeId;
    private String status;
    private CustomerDto customerDto;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CustomerDto getCustomerModel() {
        return customerDto;
    }

    public void setCustomerModel(CustomerDto customerDto) {
        this.customerDto = customerDto;
    }

    @Override
    public String toString() {
        return "invoiceDto{" +
                "id=" + id +
                ", StripeId='" + StripeId + '\'' +
                ", status='" + status + '\'' +
                ", customerModel=" + customerDto +
                '}';
    }
}
