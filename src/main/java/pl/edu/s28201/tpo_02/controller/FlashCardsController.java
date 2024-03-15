package pl.edu.s28201.tpo_02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.model.Language;
import pl.edu.s28201.tpo_02.model.SortingType;
import pl.edu.s28201.tpo_02.repository.EntryRepository;
import pl.edu.s28201.tpo_02.service.EntrySortingService;
import pl.edu.s28201.tpo_02.service.FileService;
import pl.edu.s28201.tpo_02.service.PrintingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Controller
public class FlashCardsController {
    private final EntryRepository entryRepository;
    private final FileService fileService;

    private final PrintingService printingService;

    private final EntrySortingService entrySortingService;
    private final BufferedReader console;

    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;

    @Autowired
    public FlashCardsController(EntryRepository entryRepository, FileService fileService, PrintingService printingService, EntrySortingService entrySortingService, @Qualifier("getConsoleReader") BufferedReader console) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.printingService = printingService;
        this.entrySortingService = entrySortingService;
        this.console = console;
    }

    public void listenToCommands() throws IOException {
        fileService.readFromCsv();
        System.out.println("PROFILE TYPE: " + ACTIVE_PROFILE);
        System.out.println("Use 'help' command for reference.");
        System.out.print("Type your command here: ");
        String command = console.readLine();
        System.out.println();
        while (!command.equalsIgnoreCase("exit")) {
            if (!executeCommand(command)) System.out.println("Invalid command. Use 'help' command for reference.");
            System.out.print("Type your command here: ");
            command = console.readLine();
            System.out.println();
        }
    }

    private boolean executeCommand(String command) throws IOException {
        command = command.trim();
        String[] splitCommands = command.split("\\s");
        return switch (splitCommands[0]) {
            case "print" -> executePrint(entryRepository.findAll());
            case "help" -> executeHelp();
            case "add" -> {
                if (splitCommands.length == 4) {
                    yield executeAdd(splitCommands[1], splitCommands[2], splitCommands[3]);
                }
                yield false;
            }
            case "testme" -> executeTest();
            case "sort" -> {
                if (splitCommands.length == 3) yield executeSort(splitCommands[1], splitCommands[2]);
                yield false;
            }
            default -> false;
        };
    }

    private boolean executePrint(List<Entry> entries) {
        entries.forEach(entry -> {
            System.out.println("ENG: " + printingService.process(entry.getWordEnglish()) + " <> DEU: " +
                    printingService.process(entry.getWordPolish()) + " <> " +
                    printingService.process(entry.getWordPolish()));
        });
        return true;
    }

    private boolean executeSort(String lan, String sortingType) {
        List<Entry> words = entryRepository.findAll();
        Language language = Language.valueOf(lan.toUpperCase());
        SortingType type = SortingType.valueOf(sortingType.toUpperCase());
        return switch (type) {
            case ASC -> executePrint(entrySortingService.sortByLanguageAsc(language, words));
            case DESC -> executePrint(entrySortingService.sortByLanguageDesc(language, words));
        };
    }

    private boolean executeTest() throws IOException {
        Entry random = getRandomEntry();

        List<Integer> choices = new ArrayList<>(List.of(0, 1, 2));
        int randLangTest = new Random().nextInt(0, 3);
        choices.remove(randLangTest);

        String testWord = randLangTest == 0 ? random.getWordEnglish() : (randLangTest == 1 ? random.getWordGerman() : random.getWordPolish());
        String testLang = randLangTest == 0 ? "English" : (randLangTest == 1 ? "German" : "Polish");

        readAndDisplayTestResult(choices.get(0), testWord, testLang, random);
        choices.remove(0);

        readAndDisplayTestResult(choices.get(0), testWord, testLang, random);

        return true;
    }

    private void readAndDisplayTestResult(int langIndex, String testWord, String testLang, Entry random) throws IOException {
        String answerWord = langIndex == 0 ? random.getWordEnglish() : (langIndex == 1 ? random.getWordGerman() : random.getWordPolish());
        String answerLang = langIndex == 0 ? "English" : (langIndex == 1 ? "German" : "Polish");

        System.out.println("What does the word {" + printingService.process(testWord) + "} from language ["
                + printingService.process(testLang) + "] mean in [" + printingService.process(answerLang) + "] language.");

        System.out.print("Type " + answerLang + " translation here: ");
        String answer = console.readLine().trim();
        System.out.println();

        if (answer.equalsIgnoreCase(answerWord)) {
            System.out.println("Congratulations! You've guessed correctly!");
        } else {
            System.out.println(":( Unfortunately but you are wrong ): Better luck next time!");
            System.out.println("Correct answer was: " + answerWord);
        }
    }

    private Entry getRandomEntry() {
        List<Entry> availableWords = entryRepository.findAll();
        return availableWords.get(new Random().nextInt(0, availableWords.size()));
    }

    private boolean executeAdd(String wordEng, String wordGe, String wordPol) throws IOException {
        Entry newWord = new Entry(wordEng, wordGe, wordPol);

        entryRepository.addEntry(newWord);
        fileService.addToCsv(newWord);

        return true;
    }

    private boolean executeHelp() {
        System.out.println("Commands List: ------------------------------->");
        System.out.println("help   <>  list available commands.");
        System.out.println("print   <>  print all words in csv.");
        System.out.println("add englishWord germanWord polishWord  <> add new entry to dictionary.");
        System.out.println("testme  <>  display random word from dictionary, after which you will need to write the translation in required language.");
        System.out.println("sort lang desc <>  sort by langauge. First parameter means language (pl, en, de). Second means descending or ascending order (asc or desc).");
        System.out.println("---------------------------------------------->");
        return true;
    }
}
