package com.olam.node.unit;

import com.olam.node.exceptions.UBLMessageNotValidException;
import com.olam.node.service.application.ValidationService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class ValidationServiceTest {

    private ValidationService validator;

    @Before
    public void setUp() throws Exception {
        validator = new ValidationService();
    }

    @Test
    public void validateBusinessMessageValid() throws IOException, UBLMessageNotValidException {
        String doc = "testXML/order-test-good.xml";
        File messageFile = ResourceUtils.getFile("classpath:" + doc);
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(messageFile.getAbsolutePath()), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        boolean result = validator.validateBusinessMessage(contentBuilder.toString(), "");
        assertEquals(true, result);
    }

    @Test(expected = UBLMessageNotValidException.class)
    public void validateBusinessMessageinValid() throws IOException, UBLMessageNotValidException {
        String doc = "testXML/order-test-bad1.xml";
        File messageFile = ResourceUtils.getFile("classpath:" + doc);
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(messageFile.getAbsolutePath()), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        boolean result = validator.validateBusinessMessage(contentBuilder.toString(), "");
    }
}