package pl.edu.s28201.tpo_02.repository;

import org.springframework.stereotype.Repository;
import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.model.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class EntryRepository {

    private final List<Entity> words = new ArrayList<>();

    public boolean addEntity(Entity entity) {
        if (hasEntity(entity)) return false;
        else return words.add(entity);
    }

    public Optional<Entity> findByLanguageAndWordFrom(Language langFrom, String wordFrom) {
        return words.stream().filter(w -> w.getLanguageFrom().equals(langFrom) && w.getWordFrom().equalsIgnoreCase(wordFrom)).findFirst();
    }

    public boolean hasEntity(Entity entity) {
        return words.stream().anyMatch(e -> e.equals(entity));
    }

    public List<Entity> findAll() {
        return words;
    }

    public List<Entity> getMatchingTo(Entity entity) {
        List<Entity> matchingFrom = words.stream().filter(word -> word.equals(entity)).collect(Collectors.toList());
        List<Entity> matchingTo = words.stream().filter(word -> word.equals(entity.reverse())).toList();
        matchingFrom.addAll(matchingTo);
        return matchingFrom;
    }
}
