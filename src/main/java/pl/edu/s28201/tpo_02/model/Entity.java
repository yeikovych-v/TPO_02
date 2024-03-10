package pl.edu.s28201.tpo_02.model;

import lombok.Data;

@Data
public class Entity {

    private String wordEnglish;
    private String wordGerman;
    private String wordPolish;

    public Entity(String wordEnglish, String wordGerman, String wordPolish) {
        this.wordEnglish = wordEnglish;
        this.wordGerman = wordGerman;
        this.wordPolish = wordPolish;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return wordEnglish.equalsIgnoreCase(entity.wordEnglish) && wordGerman.equalsIgnoreCase(entity.wordGerman) && wordPolish.equalsIgnoreCase(entity.wordPolish);
    }
}
