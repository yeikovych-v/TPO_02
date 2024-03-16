package pl.edu.s28201.tpo_02.service.mock;

import org.springframework.stereotype.Service;
import pl.edu.s28201.tpo_02.model.Entry;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntryMockService {

    public List<Entry> mockEntries() {
        List<Entry> entries = new ArrayList<>();
        entries.add(new Entry("aaa", "aaa", "aaa"));
        entries.add(new Entry("cat", "Katze", "kot"));
        entries.add(new Entry("dog", "Hund", "pies"));
        entries.add(new Entry("mouse", "Maus", "mysz"));
        entries.add(new Entry("stone", "Stein", "kamieñ"));
        entries.add(new Entry("tree", "Baum", "drzewo"));
        entries.add(new Entry("river", "Fluss", "rzeka"));
        entries.add(new Entry("ear", "Ohr", "ucho"));
        entries.add(new Entry("forest", "Wald", "las"));
        entries.add(new Entry("Cabinet", "Kabinett", "szafka"));
        entries.add(new Entry("bed", "Bett", "³ó¿ko"));
        entries.add(new Entry("zzz", "zzz", "zzz"));
        entries.add(new Entry("rug", "Teppich", "dywan"));
        entries.add(new Entry("window", "Fenster", "okno"));
        entries.add(new Entry("lake", "See", "jezioro"));
        entries.add(new Entry("sky", "Himmel", "niebo"));
        entries.add(new Entry("cloud", "Wolke", "chmura"));
        return entries;
    }
}
