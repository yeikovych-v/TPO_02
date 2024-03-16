package pl.edu.s28201.tpo_02.service.printing;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("upper")
@Service
public class UcPrintingService implements PrintingService{
    @Override
    public String process(String word) {
        return word.toUpperCase();
    }
}
