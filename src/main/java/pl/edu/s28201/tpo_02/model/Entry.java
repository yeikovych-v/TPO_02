package pl.edu.s28201.tpo_02.model;

import lombok.Data;

@Data
public class Entry {

    private String wordEnglish;
    private String wordGerman;
    private String wordPolish;

    public Entry(String wordEnglish, String wordGerman, String wordPolish) {
        this.wordEnglish = wordEnglish;
        this.wordGerman = wordGerman;
        this.wordPolish = wordPolish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entry entry)) return false;
        return wordEnglish.equalsIgnoreCase(entry.wordEnglish) && wordGerman.equalsIgnoreCase(entry.wordGerman) && wordPolish.equalsIgnoreCase(entry.wordPolish);
    }
}
