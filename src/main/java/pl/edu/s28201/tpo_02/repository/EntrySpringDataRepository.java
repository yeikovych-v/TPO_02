package pl.edu.s28201.tpo_02.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.model.Language;

import java.util.List;
import java.util.Optional;

public interface EntrySpringDataRepository extends CrudRepository<Entry, Long> {

    List<Entry> findAll();

    default Optional<Entry> findByLanguageAndWord(Language language, String word) {
        return switch (language) {
            case EN -> findByWordEnglish(word);
            case DE -> findByWordGerman(word);
            case PL -> findByWordPolish(word);
        };
    }

    Optional<Entry> findByWordEnglish(String word);
    Optional<Entry> findByWordGerman(String word);
    Optional<Entry> findByWordPolish(String word);

    default void updateEntryById(Long id, Entry modify) {
        Entry e = findById(id).orElseThrow();
        e.setWordEnglish(modify.getWordEnglish());
        e.setWordGerman(modify.getWordGerman());
        e.setWordPolish(modify.getWordPolish());
        save(e);
    }
}
