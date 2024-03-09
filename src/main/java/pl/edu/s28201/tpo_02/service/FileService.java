package pl.edu.s28201.tpo_02.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.exception.FileFormatException;
import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.model.Language;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.io.*;

@Service
public class FileService {

    private final EntryRepository entryRepository;
    private File csvFile;

    @Autowired
    public FileService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void readFromCsv(File file) throws IOException {
        csvFile = file;
        try (BufferedReader read = new BufferedReader(new FileReader(csvFile))) {
            StringBuilder contents = new StringBuilder();
            String line;
            while ((line = read.readLine()) != null) {
                contents.append(line);
            }
            parseAndAddToRepository(contents.toString());
        }
    }

    private void parseAndAddToRepository(String contents) {
        String[] entities = contents.split(";");
        for (String entity : entities) entryRepository.addEntity(parseCsvLineToEntity(entity));
    }

    private Entity parseCsvLineToEntity(String line) {
        String[] values = line.split(",");
        if (values.length != 4) throw new FileFormatException("Invalid number of arguments for dictionary csv file.");
        return new Entity(Language.valueOf(values[0]), values[1], Language.valueOf(values[2]), values[3]);
    }

    public void addToCsv(Entity entity) {
        writeToCsv(parseEntityToCsvLine(entity));
    }

    @SneakyThrows
    public void writeToCsv(String line) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter(csvFile, true))) {
            write.write(line);
        }
    }

    private String parseEntityToCsvLine(Entity entity) {
        return entity.getLanguageFrom() + "," + entity.getWordFrom() + "," + entity.getLanguageTo() + "," + entity.getWordTo() + ";";
    }
}
