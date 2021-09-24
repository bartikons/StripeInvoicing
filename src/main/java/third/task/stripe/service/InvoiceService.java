package third.task.stripe.service;

import org.modelmapper.ModelMapper;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceCollection;
import com.stripe.model.InvoiceItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import third.task.stripe.dto.CustomerDto;
import third.task.stripe.dto.InvoiceDto;
import third.task.stripe.model.CustomerModel;
import third.task.stripe.model.InvoiceModel;
import third.task.stripe.repository.CustomerRepository;
import third.task.stripe.repository.InvoiceRepository;

import java.util.*;

@Service
public class InvoiceService {

    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    @Value("${Stripe.apiKey}")
    private String StripeKey;
    ModelMapper mapper = new ModelMapper();

    public InvoiceService(CustomerRepository customerRepository, InvoiceRepository invoiceRepository) {
        this.customerRepository = customerRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public ResponseEntity<InvoiceDto> createInvoice(Long customerId) throws StripeException {
        Optional<CustomerModel> customerModel = customerRepository.findById(customerId);
        if (customerModel.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> params = new HashMap<>();
        InvoiceModel invoiceModel = new InvoiceModel();
        invoiceModel.setCustomerModel(customerModel.get());
        invoiceModel = invoiceRepository.save(invoiceModel);

        Stripe.apiKey = StripeKey;


        Map<String, String> initialMetadata = new HashMap<>();
        initialMetadata.put("db_id", invoiceModel.getId().toString());

        params.put("customer", customerModel.get().getStripeId());
        params.put("metadata", initialMetadata);

        Map<String, Object> invoiceItemMap = new HashMap<>();
        invoiceItemMap.put("customer", customerModel.get().getStripeId());
        invoiceItemMap.put("currency", "PLN");
        invoiceItemMap.put("amount", 2);

        InvoiceItem.create(invoiceItemMap);
        Invoice invoice = Invoice.create(params);

        invoiceModel.setStripeId(invoice.getId());
        invoiceModel.setCustomerModel(customerModel.get());
        invoiceModel.setStatus(invoice.getStatus());
        invoiceRepository.save(invoiceModel);
        InvoiceDto invoiceDto = mapper.map(invoiceModel, InvoiceDto.class);
        invoiceDto.setCustomerModel(mapper.map(customerModel.get(), CustomerDto.class));
        return new ResponseEntity<>(invoiceDto, HttpStatus.OK);
    }

    public ResponseEntity<InvoiceDto> retrieveInvoice(Long invoiceId) throws StripeException {
        Optional<InvoiceModel> invoiceModelOptional = invoiceRepository.findById(invoiceId);
        if (invoiceModelOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }

        Stripe.apiKey = StripeKey;
        Invoice invoice =
                Invoice.retrieve(invoiceModelOptional.get().getStripeId());

        ModelMapper mapper = new ModelMapper();
        InvoiceDto invoiceDto = mapper.map(invoiceModelOptional.get(), InvoiceDto.class);
        invoiceDto.setCustomerModel(mapper.map(invoiceModelOptional.get().getCustomerModel(), CustomerDto.class));
        return new ResponseEntity<>(invoiceDto, HttpStatus.OK);
    }

    public ResponseEntity<List<InvoiceDto>> retrieveInvoices(Long CustomerId, String statusOfInvoice) throws StripeException {

        Map<String, Object> params = new HashMap<>();
        Optional<CustomerModel> optionalCustomerModel = customerRepository.findById(CustomerId);
        if (optionalCustomerModel.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        params.put("customer", optionalCustomerModel.get().getStripeId());
        if (!statusOfInvoice.isEmpty()) {
            params.put("status", statusOfInvoice);
        }

        Stripe.apiKey = StripeKey;
        InvoiceCollection invoices = Invoice.list(params);
        List<InvoiceDto> invoiceDtosList = new ArrayList<>();
        for (Invoice invoice : invoices.getData()) {
            Optional<InvoiceModel> optionalInvoiceModel;
            if (!statusOfInvoice.isEmpty()) {
                optionalInvoiceModel = invoiceRepository.findByStripeIdAndStatus(invoice.getId(), statusOfInvoice);
            } else {
                optionalInvoiceModel = invoiceRepository.findByStripeId(invoice.getId());
            }
            optionalInvoiceModel.ifPresent(invoiceModel -> invoiceDtosList.add(mapper.map(invoiceModel, InvoiceDto.class)));
        }

        return new ResponseEntity<>(invoiceDtosList, HttpStatus.OK);
    }
}
