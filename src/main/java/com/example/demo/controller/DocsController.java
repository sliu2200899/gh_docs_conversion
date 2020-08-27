package com.example.demo.controller;

import com.example.demo.service.DocConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class DocsController {

    @Autowired
    DocConverter docConverter;

    @GetMapping(value = "converter",  produces = {MediaType.APPLICATION_JSON_VALUE})
    public String Converter() {
        try {
           docConverter.fileConverter();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "done";
    }

    @GetMapping(value = "/",  produces = {MediaType.APPLICATION_JSON_VALUE})
    public String healthCheck() {
        return "heartbeat....";
    }
}
