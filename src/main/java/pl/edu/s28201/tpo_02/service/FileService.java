package pl.edu.s28201.tpo_02.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.exception.FileFormatException;
import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.io.*;
import java.nio.charset.Charset;

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
        try (BufferedReader read = new BufferedReader(new FileReader(csvFile, Charset.forName("CP1252")))) {
            String line;
            while ((line = read.readLine()) != null) {
                entryRepository.addEntity(parseCsvLineToEntity(line));
            }
        }
    }

    private Entity parseCsvLineToEntity(String line) {
        String[] values = line.split(";");
        if (values.length != 3) throw new FileFormatException("Invalid number of arguments for dictionary csv file.");
        return new Entity(values[0], values[1], values[2]);
    }

    public void addToCsv(Entity entity) {
        writeToCsv(parseEntityToCsvLine(entity));
    }

    @SneakyThrows
    public void writeToCsv(String line) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter(csvFile, Charset.forName("CP1252"),true))) {
            write.write(line);
        }
    }

    private String parseEntityToCsvLine(Entity entity) {
        return entity.getWordEnglish() + ";" + entity.getWordGerman() + ";" + entity.getWordPolish() + "\n";
    }
}
