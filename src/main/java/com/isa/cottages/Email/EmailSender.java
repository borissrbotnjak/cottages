package com.isa.cottages.Email;

public interface EmailSender {
    void sendEmail(String to, String body, String topic);
}
