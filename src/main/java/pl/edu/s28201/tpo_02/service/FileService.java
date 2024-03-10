package pl.edu.s28201.tpo_02.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.exception.FileFormatException;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.io.*;
import java.nio.charset.Charset;

@Service
public class FileService {

    private final EntryRepository entryRepository;
    private File csvFile;

    @Value("${pl.edu.pja.tpo02.filename}")
    private String dictionaryPath;

    @Autowired
    public FileService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public void readFromCsv() throws IOException {
        csvFile = new File(dictionaryPath);
        try (BufferedReader read = new BufferedReader(new FileReader(csvFile, Charset.forName("CP1252")))) {
            String line;
            while ((line = read.readLine()) != null) {
                entryRepository.addEntry(parseCsvLineToEntry(line));
            }
        }
    }

    private Entry parseCsvLineToEntry(String line) {
        String[] values = line.split(";");
        if (values.length != 3) throw new FileFormatException("Invalid number of arguments for dictionary csv file.");
        return new Entry(values[0], values[1], values[2]);
    }

    public void addToCsv(Entry entry) {
        writeToCsv(parseEntryToCsvLine(entry));
    }

    @SneakyThrows
    public void writeToCsv(String line) {
        try (BufferedWriter write = new BufferedWriter(new FileWriter(csvFile, Charset.forName("CP1252"),true))) {
            write.write(line);
        }
    }

    private String parseEntryToCsvLine(Entry entry) {
        return entry.getWordEnglish() + ";" + entry.getWordGerman() + ";" + entry.getWordPolish() + "\n";
    }
}
