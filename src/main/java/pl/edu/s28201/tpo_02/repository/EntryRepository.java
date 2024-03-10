package pl.edu.s28201.tpo_02.repository;

import org.springframework.stereotype.Repository;
import pl.edu.s28201.tpo_02.model.Entity;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EntryRepository {

    private final List<Entity> words = new ArrayList<>();

    public boolean addEntity(Entity entity) {
        if (hasEntity(entity)) return false;
        else return words.add(entity);
    }

    public boolean hasEntity(Entity entity) {
        return words.stream().anyMatch(e -> e.equals(entity));
    }

    public List<Entity> findAll() {
        return words;
    }
}
