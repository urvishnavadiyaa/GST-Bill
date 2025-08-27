package com.exampl.GST.Bill.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromWhatsAppNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }

    public void UnsuccessfullyBuy(String toPhoneNumber, String messageText) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+toPhoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+fromWhatsAppNumber),
                messageText
        ).create();
        System.out.println("Sent message SID: " + message.getSid());
    }

    public void successfullyBuy(String toPhoneNumber, String messageText) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:"+toPhoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+fromWhatsAppNumber),
                messageText
        ).create();
    }

    public void UpdateMassage(String toPhoneNumber, String messageText) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + toPhoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+fromWhatsAppNumber),
                messageText
        ).create();
    }

    public void OutOfStoc(String toPhoneNumber, String messageText) {
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("whatsapp:" + toPhoneNumber),
                new com.twilio.type.PhoneNumber("whatsapp:"+fromWhatsAppNumber),
                messageText
        ).create();
    }
}
