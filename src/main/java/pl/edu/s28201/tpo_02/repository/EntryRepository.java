package pl.edu.s28201.tpo_02.repository;

import org.springframework.stereotype.Repository;
import pl.edu.s28201.tpo_02.model.Entry;

import java.util.ArrayList;
import java.util.List;

@Repository
public class EntryRepository {

    private final List<Entry> words = new ArrayList<>();

    public boolean addEntry(Entry entry) {
        if (hasEntry(entry)) return false;
        else return words.add(entry);
    }

    public boolean hasEntry(Entry entry) {
        return words.stream().anyMatch(e -> e.equals(entry));
    }

    public List<Entry> findAll() {
        return words;
    }
}
