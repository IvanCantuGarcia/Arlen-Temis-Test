package com.temis.app.service;

import com.temis.app.model.DocumentSummarizeDTO;
import jakarta.mail.MessagingException;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;

import java.io.IOException;

public interface EmailService {

    void SendHtmlEmail(String to, String subject, String body) throws MessagingException;

    void SendHtmlEmailWithAttachments(String to, String subject, String body, Pair<String, ByteArrayResource>... attachments) throws MessagingException;

    void SendSimpleEmail(String to, String subject, String body);

}