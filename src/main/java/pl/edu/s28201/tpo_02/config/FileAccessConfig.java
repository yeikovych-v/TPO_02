package pl.edu.s28201.tpo_02.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.Charset;

@Configuration
public class FileAccessConfig {

    @Value("${pl.edu.pja.tpo02.filename}")
    private String dictionaryPath;

    @SneakyThrows
    @Bean
    public BufferedReader getFileReader() {
        File csvFile = new File(dictionaryPath);
        return new BufferedReader(new FileReader(csvFile, Charset.forName("CP1252")));
    }

    @SneakyThrows
    @Bean
    public BufferedWriter getFileWriter() {
        File csvFile = new File(dictionaryPath);
        return new BufferedWriter(new FileWriter(csvFile, Charset.forName("CP1252"), true));
    }
}
