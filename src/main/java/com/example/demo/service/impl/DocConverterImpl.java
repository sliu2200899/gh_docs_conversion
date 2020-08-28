package com.example.demo.service.impl;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.example.demo.common.RandomFileName;

import com.example.demo.property.FileStorageProperties;
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
    private FileStorageProperties fileStorageProperties;

    @Override
    public void fileConverter(String fileName, String outputName) throws NullPointerException, IOException, InterruptedException, ExecutionException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();

        logger.info("++++++++++++++");
        logger.info(fileStorageProperties.getUploadDir() + File.separator + fileName);
        InputStream in = new BufferedInputStream(new FileInputStream(fileStorageProperties.getUploadDir() + File.separator + fileName));
        IConverter converter = LocalConverter.builder()
                .baseFolder(new File(fileStorageProperties.getUploadDir() + File.separator + "test"))
                .workerPool(20, 25, 2, TimeUnit.SECONDS)
                .processTimeout(5, TimeUnit.SECONDS)
                .build();

        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.DOCX)
                .to(bo).as(DocumentType.PDF)
                .prioritizeWith(1000) // optional
                .schedule();
        conversion.get();

        try (OutputStream outputStream = new FileOutputStream(fileStorageProperties.getDownloadDir() + File.separator + outputName)) {
            bo.writeTo(outputStream);
        }
        in.close();
        bo.close();
    }
}
