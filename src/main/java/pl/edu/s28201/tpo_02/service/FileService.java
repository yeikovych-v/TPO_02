package pl.edu.s28201.tpo_02.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.exception.FileFormatException;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.repository.EntryRepository;

import java.io.*;

@Service
public class FileService {

    private final EntryRepository entryRepository;
    private BufferedReader read;
    private BufferedWriter write;

    @Autowired
    public FileService(EntryRepository entryRepository, @Qualifier("getFileReader") BufferedReader read, BufferedWriter write) {
        this.entryRepository = entryRepository;
        this.read = read;
        this.write = write;
    }

    public void readFromCsv() throws IOException {
        String line;
        while ((line = read.readLine()) != null) {
            entryRepository.addEntry(parseCsvLineToEntry(line));
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
        write.write(line);
    }

    private String parseEntryToCsvLine(Entry entry) {
        return entry.getWordEnglish() + ";" + entry.getWordGerman() + ";" + entry.getWordPolish() + "\n";
    }
}
