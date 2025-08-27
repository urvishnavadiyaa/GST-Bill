package com.exampl.GST.Bill.Service;

import com.exampl.GST.Bill.Exception.CustomException;
import com.exampl.GST.Bill.Model.Product;
import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Repository.BillRepository;
import com.exampl.GST.Bill.Repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class BillService {
    @Autowired
    BillRepository billRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    TwilioService twilioService;

    public String saveProduct(List<Product> products) {
        billRepository.saveAll(products);
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

        Product product = billRepository.findByProductName(customers.getProduct_name());
        if (product == null) {
            throw new CustomException("637", "no product found");
        }

        int amount;
        try {
            amount = billRepository.getProductAmount(customers.getProduct_name());
            int c = billRepository.getProductCount(customers.getProduct_name());
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

//        int amount;
//        try {
//            amount = billRepository.getProductAmount(customers.getProduct_name());
//        } catch (Exception e) {
//            throw new CustomException("606", "something went wrong" + e.getMessage());
//        }

        try {
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

            Customer customer = new Customer(customers.getCustId(), customers.getProduct_name(),
                    customers.getMobile_number(), customers.getProduct_name(),
                    customers.getProduct_count(), amount, total_amount, ld);
            customerRepository.save(customer);

            product.setStock(product.getStock() - customers.getProduct_count());
            twilioService.successfullyBuy(
                    "+91" + customers.getMobile_number(),
                    "You have successfully bought:"
            );

            billRepository.save(product);
        } finally {
            if (product.getStock() <= 5) {
                twilioService.UpdateMassage(
                        "+91" + customers.getMobile_number(),
                        "please Update Stock"
                );
            }
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
