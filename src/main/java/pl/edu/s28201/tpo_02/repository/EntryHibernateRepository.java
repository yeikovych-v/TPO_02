package pl.edu.s28201.tpo_02.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.model.Language;

import java.util.List;
import java.util.Optional;

@Repository
public class EntryHibernateRepository {

    private final EntityManager entityManager;

    public EntryHibernateRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Entry> findAll() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Entry> query = criteriaBuilder.createQuery(Entry.class);

        query.select(query.from(Entry.class));

        return entityManager.createQuery(query).getResultList();
    }

    public void saveAll(List<Entry> entries) {
        entries.forEach(this::save);
    }

    @Transactional
    public void save(Entry entry) {
        entityManager.persist(entry);
    }

    public Optional<Entry> findByLanguageAndWord(Language language, String word) {
        return switch (language) {
            case EN -> findByWordEnglish(word);
            case DE -> findByWordGerman(word);
            case PL -> findByWordPolish(word);
        };
    }

    public Optional<Entry> findByWordEnglish(String word) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Entry> query = criteriaBuilder.createQuery(Entry.class);

        Root<Entry> root = query.from(Entry.class);
        query.select(root).where(criteriaBuilder.equal(root.get("wordEnglish"), word));

        try {
            Entry entry = entityManager.createQuery(query).setMaxResults(1).getSingleResult();
            return Optional.of(entry);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Entry> findByWordGerman(String word) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Entry> query = criteriaBuilder.createQuery(Entry.class);

        Root<Entry> root = query.from(Entry.class);
        query.select(root).where(criteriaBuilder.equal(root.get("wordGerman"), word));

        try {
            Entry entry = entityManager.createQuery(query).setMaxResults(1).getSingleResult();
            return Optional.of(entry);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Entry> findByWordPolish(String word) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Entry> query = criteriaBuilder.createQuery(Entry.class);

        Root<Entry> root = query.from(Entry.class);
        query.select(root).where(criteriaBuilder.equal(root.get("wordPolish"), word));

        try {
            Entry entry = entityManager.createQuery(query).setMaxResults(1).getSingleResult();
            return Optional.of(entry);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    public void updateEntryById(Long id, Entry modified) {
        Entry entry = entityManager.find(Entry.class, id);

        entry.setWordEnglish(modified.getWordEnglish());
        entry.setWordGerman(modified.getWordGerman());
        entry.setWordPolish(modified.getWordPolish());

        entityManager.merge(entry);
    }

    @Transactional
    public void delete(Entry toDelete) {
        Entry entry = entityManager.find(Entry.class, toDelete.getId());

        entityManager.remove(entry);
    }
}
