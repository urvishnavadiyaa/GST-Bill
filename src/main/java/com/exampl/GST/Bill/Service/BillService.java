package com.exampl.GST.Bill.Service;

import com.exampl.GST.Bill.Exception.CustomException;
import com.exampl.GST.Bill.Model.Product;
import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Repository.ProductRepository;
import com.exampl.GST.Bill.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Service
public class BillService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TwilioService twilioService;

    public String saveProduct(List<Product> products) {
        productRepository.saveAll(products);
        return "Receive All Product Success Fully";
    }

    public String saveCustomer(Customer customers) throws CustomException {
        if (customers.getCustomer_name().isEmpty()) {
            throw new CustomException("601", "enter name");
        }

        if (!customers.getMobile_number().matches("\\d{10}")) {
            throw new CustomException("602", "enter valid 10 digit mobile no");
        }

        if (customers.getProduct_name().isEmpty()) {
            throw new CustomException("603", "please select product");
        }

        if (customers.getProduct_count() <= 0) {
            throw new CustomException("608", "please enter valid product count");
        }

        Product product = productRepository.findByProductName(customers.getProduct_name());
        if (product == null) {
            throw new CustomException("637", "no product found");
        }

        int amount;
        try {
            amount = productRepository.getProductAmount(customers.getProduct_name());
            int c = productRepository.getProductCount(customers.getProduct_name());
            if (customers.getProduct_count() > c) {
                twilioService.OutOfStoc(
                        "+91" + customers.getMobile_number(),
                        "We not Enough Stock for this product"
                );
                throw new CustomException("604", "there are no stock available");
            }
        } catch (Exception e) {
            throw new CustomException("605", "something went wrong " + e.getMessage());
        }

        int totalamt = getTotalAmount(customers.getProduct_count(), amount);
        double total_amount = totalamt + calculate_Gst(totalamt);
        LocalDate ld = LocalDate.now();

        boolean flag = checkPaymentStatus();

        if (!flag) {
            twilioService.UnsuccessfullyBuy(
                    "+91" + customers.getMobile_number(),
                    "Payment Failed! Try again."
            );
            throw new CustomException("607", "payment faild");
        }

        Customer customer = new Customer(customers.getCustId(), customers.getCustomer_name(),
                customers.getMobile_number(), customers.getProduct_name(),
                customers.getProduct_count(), amount, total_amount, ld);
        customerRepository.save(customer);

        product.setStock(product.getStock() - customers.getProduct_count());
        twilioService.successfullyBuy(
                "+91" + customers.getMobile_number(),
                "You have successfully bought:"
        );

        productRepository.save(product);
        if (product.getStock() <= product.getThreshHold()) {
            twilioService.UpdateMessage(
                    "+91" + customers.getMobile_number(),
                    "please Update Stock"
            );
        }

        return "Payment SuccessFul";
    }

    private int getTotalAmount(int count, int amount) {
        return count * amount;
    }

    public boolean checkPaymentStatus() {
        Random random = new Random();
        return random.nextBoolean();
    }

    public double calculate_Gst(long value) {
        return value * (18.0 / 100);
    }

}
