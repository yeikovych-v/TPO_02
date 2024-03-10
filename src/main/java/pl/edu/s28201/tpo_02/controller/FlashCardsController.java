package pl.edu.s28201.tpo_02.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.repository.EntryRepository;
import pl.edu.s28201.tpo_02.service.DisplayService;
import pl.edu.s28201.tpo_02.service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Controller
public class FlashCardsController {
    private final EntryRepository entryRepository;
    private final FileService fileService;
    private final DisplayService displayService;
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;

    @Autowired
    public FlashCardsController(EntryRepository entryRepository, FileService fileService, DisplayService displayService) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.displayService = displayService;
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

    private boolean executeCommand(String command) {
        command = command.trim();
        String[] splitCommands = command.split("\\s");
        return switch (splitCommands[0]) {
            case "print" -> displayService.print(entryRepository.findAll());
            case "help" -> executeHelp();
            case "add" -> {
                if (splitCommands.length == 4) {
                    yield executeAdd(splitCommands[1], splitCommands[2], splitCommands[3]);
                }
                yield false;
            }
            case "testme" -> executeTest();
            default -> false;
        };
    }

    private boolean executeTest() {
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

    @SneakyThrows
    private void readAndDisplayTestResult(int langIndex, String testWord, String testLang, Entry random) {
        String answerWord = langIndex == 0 ? random.getWordEnglish() : (langIndex == 1 ? random.getWordGerman() : random.getWordPolish());
        String answerLang = langIndex == 0 ? "English" : (langIndex == 1 ? "German" : "Polish");

        System.out.println("What does the word {" + testWord + "} from language [" + testLang + "] mean in [" + answerLang + "] language.");

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

    private boolean executeAdd(String wordEng, String wordGe, String wordPol) {
        Entry newWord = new Entry(wordEng, wordGe, wordPol);

        entryRepository.addEntry(newWord);
        fileService.addToCsv(newWord);

        return true;
    }

    private boolean executeHelp() {
        System.out.println("Commands List: ------------------------------->");
        System.out.println("help   <> list available commands.");
        System.out.println("print   <> print all words in csv.");
        System.out.println("add englishWord germanWord polishWord  <> add new entry to dictionary.");
        System.out.println("testme  <> display random word from dictionary, after which you will need to write the translation in required language.");
        System.out.println("---------------------------------------------->");
        return true;
    }
}
