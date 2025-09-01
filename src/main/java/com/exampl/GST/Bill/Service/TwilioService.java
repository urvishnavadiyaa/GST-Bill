package com.exampl.GST.Bill.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioService.class);

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @Value("${twilio.phone_number}")
    private String fromWhatsAppNumber;

    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
        logger.info("Twilio initialized with SID: {}", accountSid);
    }

    private void sendWhatsAppMessage(String toPhoneNumber, String messageText) {
        try {
            Message message = Message.creator(
                    new com.twilio.type.PhoneNumber("whatsapp:" + toPhoneNumber),
                    new com.twilio.type.PhoneNumber("whatsapp:" + fromWhatsAppNumber),
                    messageText
            ).create();
            logger.info("Sent WhatsApp message SID: {}", message.getSid());
        } catch (Exception e) {
            logger.error("Failed to send WhatsApp message", e);
        }
    }

    public void successfullyBuy(String toPhoneNumber, String messageText) {
        sendWhatsAppMessage(toPhoneNumber, messageText);
    }

    public void OutOfStoc(String toPhoneNumber, String messageText) {
        sendWhatsAppMessage(toPhoneNumber, messageText);
    }

    public void UnsuccessfullyBuy(String toPhoneNumber, String messageText) {
        sendWhatsAppMessage(toPhoneNumber, messageText);
    }

    public void UpdateMassage(String toPhoneNumber, String messageText) {
        sendWhatsAppMessage(toPhoneNumber, messageText);
    }
}