package com.temis.app.controller;

import com.temis.app.entity.MessageContextEntity;
import com.temis.app.entity.ServiceEntity;
import com.temis.app.model.MessageSource;
import com.temis.app.model.ServiceState;
import com.temis.app.service.MessageService;
import com.temis.app.service.ServiceEntityService;
import com.temis.app.state.FirstContactState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/whatsapp-bot")
@Slf4j
public class WhatsappBotController {

    @Autowired
    private FirstContactState firstContactState;

    @Autowired
    private MessageService messageService;

    @PostMapping("/webhook")
    public void receiveWhatsAppMessage(@RequestParam Map<String, String> requestBody) {
        try {
            String userMessage = requestBody.get("Body");
            String phoneNumber = requestBody.get("From").replace("whatsapp:", "");
            String nickName = requestBody.get("ProfileName");
            String SmsMessageSid = requestBody.get("SmsMessageSid");

            if (userMessage == null || phoneNumber == null || SmsMessageSid == null) {
                log.error("Campos obligatorios faltantes en la solicitud: {}", requestBody);
                throw new IllegalArgumentException("Solicitud incompleta: falta Body, From o SmsMessageSid");
            }

            log.info("Mensaje recibido: {}", requestBody);


            var messageContextBuilder = MessageContextEntity.builder()
                    .messageId("twilio:" + SmsMessageSid)
                    .phoneNumber(phoneNumber.replace("whatsapp:", ""))
                    .nickName(nickName)
                    .body(userMessage)
                    .messageSource(MessageSource.TWILIO)
                    .request(new HashMap<>(requestBody));

            if ("1".equals(requestBody.get("NumMedia"))) { //TODO: Checar https://www.twilio.com/docs/messaging/api/media-resource#fetch-a-media-resource

                messageContextBuilder.mediaUrl(requestBody.get("MediaUrl0")).mediaContentType(requestBody.get("MediaContentType0"));

            }

            var response = firstContactState.Evaluate(messageContextBuilder.build());
            log.info("Respuesta generada: {}", response);

            messageService.sendResponseToUser(response);

        } catch (IllegalArgumentException e) {
            log.error("Solicitud inválida: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Error al procesar el mensaje: {}", e.getMessage(), e);
        }
    }
}