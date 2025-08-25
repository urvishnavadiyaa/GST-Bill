package com.exampl.GST.Bill.Controller;

import com.exampl.GST.Bill.Exception.ControllerException;
import com.exampl.GST.Bill.Exception.CustomException;
import com.exampl.GST.Bill.Model.Product;
import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Service.BillService;
import com.exampl.GST.Bill.Service.MailService;
import com.exampl.GST.Bill.Service.TwilioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/GST")
public class GstController {

    @Autowired
    BillService billService;

    @Autowired
    private MailService mailService;


    @PostMapping("/save-products")
    public String addProduct(@RequestBody List<Product> products) {
        return billService.saveProduct(products);
    }

    @PostMapping("/add-customer")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customers) {
        try {
            String ce = billService.saveCustomer(customers);
            return new ResponseEntity<>(ce, HttpStatus.OK);
        } catch (CustomException e) {
            ControllerException conE = new ControllerException(e.getErrorCode(), e.getErrorMessage());
            return new ResponseEntity<>(conE, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/send-email")
    public String sendEmail() {
        return mailService.sendDailyReportOnStartup();
    }
}
