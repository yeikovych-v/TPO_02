package pl.edu.s28201.tpo_02.service;

import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.model.Language;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntrySortingService {

    public List<Entry> sortByLanguageDesc(Language language, List<Entry> words) {
        return switch (language) {
            case EN -> sortEnglishDesc(words);
            case DE -> sortGermanDesc(words);
            case PL -> sortPolishDesc(words);
        };
    }

    private List<Entry> sortEnglishDesc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordEnglish).reversed()).collect(Collectors.toList());
    }

    private List<Entry> sortGermanDesc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordGerman).reversed()).collect(Collectors.toList());
    }

    private List<Entry> sortPolishDesc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordPolish).reversed()).collect(Collectors.toList());
    }

    public List<Entry> sortByLanguageAsc(Language language, List<Entry> words) {
        return switch (language) {
            case EN -> sortEnglishAsc(words);
            case DE -> sortGermanAsc(words);
            case PL -> sortPolishAsc(words);
        };
    }

    private List<Entry> sortEnglishAsc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordEnglish)).collect(Collectors.toList());
    }

    private List<Entry> sortGermanAsc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordGerman)).collect(Collectors.toList());
    }

    private List<Entry> sortPolishAsc(List<Entry> words) {
        return words.stream().sorted(Comparator.comparing(Entry::getWordPolish)).collect(Collectors.toList());
    }
}
