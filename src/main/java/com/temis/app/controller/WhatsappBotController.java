package com.temis.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.temis.app.model.DocumentSummarizeDTO;
import com.temis.app.service.SummarizeService;

@RestController
@RequestMapping("/whatsapp-bot")
public class WhatsappBotController {

    @Autowired
    private SummarizeService summarizeService;

    @GetMapping("/ping")
    public String get(){
        return "Hola Mundo";
    }

    @GetMapping("/summarizeTest")
    public String getSummarizeTest() throws IOException{
        System.out.println("CLASE::: " + this.toString());
        DocumentSummarizeDTO summary = summarizeService.getSummarizeFromDocument();
        System.out.println(summary.getSummarize());
        return summary.getSummarize();
    }
}
