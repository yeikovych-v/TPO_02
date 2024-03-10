package pl.edu.s28201.tpo_02.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.util.List;

@Profile("upper")
@Service
public class UpperCaseDisplayService implements DisplayService{

    @Override
    public boolean print(List<Entity> entities) {
        entities.forEach(entity -> System.out.println("ENG: " + entity.getWordEnglish().toUpperCase()
                + " <> GER: "  + entity.getWordGerman().toUpperCase() + " <> POL: " + entity.getWordPolish().toUpperCase()));
        System.out.println();
        return true;
    }
}
