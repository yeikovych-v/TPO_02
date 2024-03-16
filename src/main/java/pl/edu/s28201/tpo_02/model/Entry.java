package pl.edu.s28201.tpo_02.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String wordEnglish;
    @Column(unique = true)
    private String wordGerman;
    @Column(unique = true)
    private String wordPolish;


    public Entry(String wordEnglish, String wordGerman, String wordPolish) {
        this.wordEnglish = wordEnglish;
        this.wordGerman = wordGerman;
        this.wordPolish = wordPolish;
    }

    public Entry() {

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
                "id=" + id +
                ", wordEnglish='" + wordEnglish + '\'' +
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

    public Long getId() {
        return id;
    }
}
