package com.exampl.GST.Bill.Controller;

import com.exampl.GST.Bill.Exception.CustomException;
import com.exampl.GST.Bill.Model.Product;
import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Service.BillService;
import com.exampl.GST.Bill.Service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/GST")
public class GstController {

    @Autowired
    BillService billService;

    @Autowired
    MailService mailService;

    @PostMapping("/save-products")
    public String addProduct(@RequestBody List<Product> products) {
        return billService.saveProduct(products);
    }

    @PostMapping("/purchase")
    public ResponseEntity<?> addCustomer(@RequestBody Customer customers) {
        try {
            String ce = billService.saveCustomer(customers);
            return new ResponseEntity<>(ce, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/send-email/{date}")
    public String sendEmail(@PathVariable LocalDate date) {
        return mailService.sendDailyReportOnStartup(date);
    }
}
