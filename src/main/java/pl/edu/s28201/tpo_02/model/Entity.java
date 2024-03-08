package pl.edu.s28201.tpo_02.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Entity {

    private Language languageFrom;
    private Language languageTo;
    private String wordFrom;
    private String wordTo;

    public Entity(Language languageFrom, String wordFrom, Language languageTo, String wordTo) {
        this.languageFrom = languageFrom;
        this.wordFrom = wordFrom;
        this.languageTo = languageTo;
        this.wordTo = wordTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return languageFrom == entity.languageFrom && languageTo == entity.languageTo && wordFrom.equalsIgnoreCase(entity.wordFrom) && wordTo.equalsIgnoreCase(entity.wordTo);
    }
}
