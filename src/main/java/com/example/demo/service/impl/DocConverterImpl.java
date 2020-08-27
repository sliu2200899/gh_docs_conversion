package com.example.demo.service.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.example.demo.DemoApplication;
import com.example.demo.FileConfig;
import com.example.demo.service.DocConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
public class DocConverterImpl implements DocConverter {

    private static Logger logger = LoggerFactory.getLogger(DocConverterImpl.class);

    @Autowired
    private FileConfig fileConfig;

    @Override
    public void fileConverter() throws NullPointerException, IOException, InterruptedException, ExecutionException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        logger.info("++++++++++++++");
        logger.info(fileConfig.getDir() + File.separator + fileConfig.getInput());
        InputStream in = new BufferedInputStream(new FileInputStream(fileConfig.getDir() + File.separator + fileConfig.getInput()));
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(fileConfig.getDir() + File.separator + "test"))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.DOCX)
                .to(bo).as(DocumentType.PDF)
                .prioritizeWith(1000) // optional
                .schedule();
        conversion.get();
        try (OutputStream outputStream = new FileOutputStream(fileConfig.getOutput())) {
            bo.writeTo(outputStream);
        }
        in.close();
        bo.close();
    }
}
