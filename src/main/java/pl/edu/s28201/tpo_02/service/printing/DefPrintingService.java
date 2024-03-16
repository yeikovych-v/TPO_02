package pl.edu.s28201.tpo_02.service.printing;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("default")
public class DefPrintingService implements PrintingService{
    @Override
    public String process(String word) {
        return word;
    }
}
