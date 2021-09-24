package third.task.stripe.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import third.task.stripe.dto.InvoiceDto;
import third.task.stripe.security.services.AuthenticationService;
import third.task.stripe.service.InvoiceService;

import java.util.List;

@Controller
@RequestMapping("/api")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final AuthenticationService authenticationService;

    public InvoiceController(InvoiceService invoiceService, AuthenticationService authenticationService) {
        this.invoiceService = invoiceService;
        this.authenticationService = authenticationService;
    }

    @PostMapping()
    public ResponseEntity<InvoiceDto> createInvoice() {
        try {
            return invoiceService.createInvoice(authenticationService.getIdFromJwt());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping()
    public ResponseEntity<InvoiceDto> retrieveInvoice(@RequestParam("invoiceId") Long invoiceId) {
        try {
            return invoiceService.retrieveInvoice(invoiceId);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/gets")
    public ResponseEntity<List<InvoiceDto>> retrieveInvoices(@RequestParam(value = "statusOfInvoice",required = false, defaultValue = "") String statusOfInvoice) {
        try {
            return invoiceService.retrieveInvoices(authenticationService.getIdFromJwt(),statusOfInvoice);
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
