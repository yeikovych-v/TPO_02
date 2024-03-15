package pl.edu.s28201.tpo_02.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("lower")
public class LcPrintingService implements PrintingService{
    @Override
    public String process(String word) {
        return word.toLowerCase();
    }
}
