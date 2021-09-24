# StripeInvoicing

This Backend App can:

Create/login Customer

Create/view current Customer invoice

API Endoint

/api/customer/logIn         Post  (RequestBody Login)         Return JwtToken

/api/customer/CreateAccount Post  (RequestBody Login)         Return String

/api                        Post  ()                          Return InvoiceDto

/api                        Get   (RequestParam invoiceId)    Return InvoiceDto

/api/gets                   Get   (Optional StatusOfinvoice)  Return List<InvoiceDto>
