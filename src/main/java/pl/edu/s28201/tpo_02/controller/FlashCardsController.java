package pl.edu.s28201.tpo_02.controller;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.edu.s28201.tpo_02.model.Entity;
import pl.edu.s28201.tpo_02.model.Language;
import pl.edu.s28201.tpo_02.repository.EntryRepository;
import pl.edu.s28201.tpo_02.service.DisplayService;
import pl.edu.s28201.tpo_02.service.FileService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Controller
public class FlashCardsController {
    private final EntryRepository entryRepository;
    private final FileService fileService;
    private final BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;

    private DisplayService displayService;

    @Autowired
    public FlashCardsController(EntryRepository entryRepository, FileService fileService, DisplayService displayService) {
        this.entryRepository = entryRepository;
        this.fileService = fileService;
        this.displayService = displayService;
    }

    public void listenToCommands() throws IOException {
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
                if (splitCommands.length == 5) {
                    yield executeAdd(splitCommands[1], splitCommands[2], splitCommands[3], splitCommands[4]);
                }
                yield false;
            }
            case "testme" -> executeTest();
            default -> false;
        };
    }

    @SneakyThrows
    private boolean executeTest() {
        Entity random = getRandomEntity();
        System.out.println(random);
        System.out.println("What does the word {" + random.getWordFrom() + "} from language [" + random.getLanguageFrom() + "] mean in language [" + random.getLanguageTo() + "]");
        System.out.print("Type here: ");
        String answer = console.readLine().trim();

        List<Entity> matchingList = getMatchingTo(random.getLanguageFrom(), random.getWordFrom(), random.getLanguageTo(), answer);
        if (matchingList.size() > 0) {
            System.out.println("Congratulations! You've guessed correctly!");
            System.out.println("Here are the list of words with similar meaning.");
            displayService.print(matchingList);
            return true;
        }
        System.out.println(":( Unfortunately but you are wrong ): Better luck next time!");
        System.out.println("Correct answer was: " + random.getWordTo());
        return true;
    }

    private List<Entity> getMatchingTo(Language langFrom, String wordFrom, Language langTo, String wordTo) {
        return entryRepository.getMatchingTo(new Entity(langFrom, wordFrom, langTo, wordTo));
    }

    private Entity getRandomEntity() {
        List<Entity> availableWords = entryRepository.findAll();
        return availableWords.get(new Random().nextInt(0, availableWords.size() - 1));
    }

    private boolean executeAdd(String langFrom, String wordFrom, String langTo, String wordTo) {
        Language[] availableLangs = Language.values();

        if (Arrays.stream(availableLangs).noneMatch(lang -> lang.toString().equalsIgnoreCase(langFrom))) return false;
        if (Arrays.stream(availableLangs).noneMatch(lang -> lang.toString().equalsIgnoreCase(langTo))) return false;

        Entity newWord = new Entity(Language.valueOf(langFrom.toUpperCase()), wordFrom, Language.valueOf(langTo.toUpperCase()), wordTo);

        entryRepository.addEntity(newWord);
        fileService.addToCsv(newWord);

        return true;
    }

    private boolean executeHelp() {
        System.out.println("Commands List: ------------------------------->");
        System.out.println("help   <> list available commands.");
        System.out.println("print   <> print all words in csv.");
        System.out.println("add pl polishWord en englishWord  <> add new word to dictionary.");
        System.out.println("testme  <> display random word from dictionary, after which you will need to write the translation in required language.");
        System.out.println("---------------------------------------------->");
        return true;
    }
}
