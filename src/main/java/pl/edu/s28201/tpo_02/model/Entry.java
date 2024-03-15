package pl.edu.s28201.tpo_02.model;

import java.util.Objects;

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

    @Override
    public String toString() {
        return "Entry{" +
                "wordEnglish='" + wordEnglish + '\'' +
                ", wordGerman='" + wordGerman + '\'' +
                ", wordPolish='" + wordPolish + '\'' +
                '}';
    }

    public String getWordEnglish() {
        return wordEnglish;
    }

    public void setWordEnglish(String wordEnglish) {
        this.wordEnglish = wordEnglish;
    }

    public String getWordGerman() {
        return wordGerman;
    }

    public void setWordGerman(String wordGerman) {
        this.wordGerman = wordGerman;
    }

    public String getWordPolish() {
        return wordPolish;
    }

    public void setWordPolish(String wordPolish) {
        this.wordPolish = wordPolish;
    }

    @Override
    public int hashCode() {
        return Objects.hash(wordEnglish, wordGerman, wordPolish);
    }

}
