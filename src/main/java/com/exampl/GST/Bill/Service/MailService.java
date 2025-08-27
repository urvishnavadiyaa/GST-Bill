package com.exampl.GST.Bill.Service;

import com.exampl.GST.Bill.Model.Customer;
import com.exampl.GST.Bill.Repository.CustomerRepository;
import jakarta.annotation.PostConstruct;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;

@Component
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private CustomerRepository customerRepository;

    public String sendDailyReportOnStartup() {
        LocalDate today = LocalDate.now();
        List<Customer> customers = customerRepository.findByLocalDate(today);

        if (customers.isEmpty()) {
            return "No records found for today.";
        }

        File csvFile = new File("customers.csv");

        try (PrintWriter writer = new PrintWriter(csvFile)) {
            writer.println("ID,Name,Mobile,Product,Amount");

            for (Customer c : customers) {
                writer.println(c.getCustId() + "," +
                        c.getCustomer_name() + "," +
                        c.getMobile_number() + "," +
                        c.getProduct_name()+ "," +
                        c.getTotal_amount());
            }
            writer.flush();
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("urvishnavadiya2004@gmail.com");
            helper.setTo("urvishnavadiya2004@gmail.com");
            helper.setSubject("🧾 Daily Customer Report");
            helper.setText("Today's customer purchase report is attached.");

            FileSystemResource file = new FileSystemResource(csvFile);
            helper.addAttachment("CustomerReport.csv", file);

            mailSender.send(message);
            System.out.println("Mail sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            csvFile.delete();
        }
        return "something went wrong";
    }
}
