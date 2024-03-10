package pl.edu.s28201.tpo_02.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.model.Entry;

import java.util.List;

@Profile("default")
@Service
public class DefaultDisplayService implements DisplayService {
    @Override
    public boolean print(List<Entry> entities) {
        entities.forEach(entry -> System.out.println("ENG: " + entry.getWordEnglish()
                + " <> GER: "  + entry.getWordGerman() + " <> POL: " + entry.getWordPolish()));
        System.out.println();
        return true;
    }
}
