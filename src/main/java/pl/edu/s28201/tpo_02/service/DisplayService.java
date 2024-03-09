package pl.edu.s28201.tpo_02.service;

import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.util.List;

public interface DisplayService {

    boolean print(List<Entity> entities);
}
