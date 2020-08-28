package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public interface DocConverter {
    void fileConverter(String fileName, String outputName) throws NullPointerException, IOException, InterruptedException, ExecutionException, FileNotFoundException;
}
