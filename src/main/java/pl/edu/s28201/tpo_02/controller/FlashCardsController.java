package pl.edu.s28201.tpo_02.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import pl.edu.s28201.tpo_02.model.Entry;
import pl.edu.s28201.tpo_02.model.Language;
import pl.edu.s28201.tpo_02.model.SortingType;
import pl.edu.s28201.tpo_02.repository.EntrySpringDataRepository;
import pl.edu.s28201.tpo_02.service.sorting.EntrySortingService;
import pl.edu.s28201.tpo_02.service.mock.EntryMockService;
import pl.edu.s28201.tpo_02.service.printing.PrintingService;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

@Controller
public class FlashCardsController {
    private final EntrySpringDataRepository entryRepository;
    private final PrintingService printingService;
    private final EntrySortingService entrySortingService;
    private final EntryMockService entryMockService;
    private final BufferedReader console;

    @Value("${spring.profiles.active}")
    private String ACTIVE_PROFILE;

    @Autowired
    public FlashCardsController(EntrySpringDataRepository entryRepository,
                                PrintingService printingService,
                                EntrySortingService entrySortingService,
                                EntryMockService entryMockService,
                                BufferedReader console) {
        this.entryRepository = entryRepository;
        this.printingService = printingService;
        this.entrySortingService = entrySortingService;
        this.entryMockService = entryMockService;
        this.console = console;
    }

    public void listenToCommands() throws IOException {
        createDictionaryIfNeeded();
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

    private void createDictionaryIfNeeded() {
        if (entryRepository.findAll().isEmpty()) entryRepository.saveAll(entryMockService.mockEntries());
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
            case "search" -> {
                if (splitCommands.length == 3) yield executeSearch(splitCommands[1], splitCommands[2]);
                yield false;
            }
            case "modify" -> {
                if (splitCommands.length == 4)
                    yield executeModify(splitCommands[1], splitCommands[2], splitCommands[3]);
                yield false;
            }
            case "delete" -> {
                if (splitCommands.length == 3) yield executeDelete(splitCommands[1], splitCommands[2]);
                yield false;
            }
            default -> false;
        };
    }

    private String getStringRecordFromEntry(Entry entry) {
        return "ENG: " + printingService.process(entry.getWordEnglish()) + " <> DEU: " +
                printingService.process(entry.getWordGerman()) + " <> POL: " +
                printingService.process(entry.getWordPolish());
    }

    //  ----------------- PRINT -----------------
    private boolean executePrint(List<Entry> entries) {
        entries.forEach(entry -> System.out.println(getStringRecordFromEntry(entry)));
        System.out.println();
        return true;
    }

    //  ----------------- ADD -----------------
    private boolean executeAdd(String wordEng, String wordGe, String wordPol) {
        Entry newWord = new Entry(wordEng, wordGe, wordPol);

        if (!isUniqueValue(newWord)) {
            printEntryNotUniqueMessage(newWord);
            return true;
        }

        entryRepository.save(newWord);
        printEntryAddedMessage(newWord);

        return true;
    }

    //  ----------------- TEST -----------------
    private boolean executeTest() throws IOException {
        Entry random = getRandomEntry();

        List<Integer> choices = new ArrayList<>(List.of(0, 1, 2));
        int randLangTest = new Random().nextInt(0, 3);
        choices.remove(randLangTest);

        String testWord = randLangTest == 0 ? random.getWordEnglish() : (randLangTest == 1 ? random.getWordGerman() : random.getWordPolish());
        String testLang = randLangTest == 0 ? "English" : (randLangTest == 1 ? "German" : "Polish");

        readAndDisplayTestResult(choices.getFirst(), testWord, testLang, random);
        choices.removeFirst();

        readAndDisplayTestResult(choices.getFirst(), testWord, testLang, random);

        return true;
    }

    private Entry getRandomEntry() {
        List<Entry> availableWords = entryRepository.findAll();
        return availableWords.get(new Random().nextInt(0, availableWords.size()));
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

    //  ----------------- SORT -----------------
    private boolean executeSort(String lang, String sortingType) {
        List<Entry> words = processAll(entryRepository.findAll());
        if (!isValidLang(lang)) return false;
        if (!isValidSortingType(sortingType)) return false;
        Language language = Language.valueOf(lang.toUpperCase());
        SortingType type = SortingType.valueOf(sortingType.toUpperCase());
        return switch (type) {
            case ASC -> executePrint(entrySortingService.sortByLanguageAsc(language, words));
            case DESC -> executePrint(entrySortingService.sortByLanguageDesc(language, words));
        };
    }

    private List<Entry> processAll(List<Entry> entries) {
        List<Entry> processed = new ArrayList<>();
        entries.forEach(entry -> {
            String processedEng = printingService.process(entry.getWordEnglish());
            String processedGer = printingService.process(entry.getWordGerman());
            String processedPol = printingService.process(entry.getWordPolish());
            processed.add(new Entry(processedEng, processedGer, processedPol));
        });
        return processed;
    }

    //  ----------------- SEARCH -----------------
    private boolean executeSearch(String lang, String word) {
        if (!isValidLang(lang)) return false;
        Language language = Language.valueOf(lang.toUpperCase());
        Optional<Entry> optionalWord = entryRepository.findByLanguageAndWord(language, word);

        if (entryNotFound(word, optionalWord, language)) return true;

        printEntryIsPresentMessage(optionalWord.get());
        System.out.println();

        return true;
    }

    //  ----------------- MODIFY -----------------
    private boolean executeModify(String lang, String wordBeforeMod, String wordAfterMod) {
        if (!isValidLang(lang)) return false;
        Language language = Language.valueOf(lang.toUpperCase());
        Optional<Entry> optionalWord = entryRepository.findByLanguageAndWord(language, wordBeforeMod);

        if (entryNotFound(wordBeforeMod, optionalWord, language)) return true;

        Entry toModify = optionalWord.get();
        Entry modified = modifyEntry(toModify, language, wordAfterMod);

        if (!isUniqueValue(language, wordAfterMod)) {
            printEntryNotUniqueMessage(modified);
            return true;
        }

        entryRepository.updateEntryById(toModify.getId(), modified);
        printEntryModifiedMessage(toModify, modified);

        return true;
    }

    private Entry modifyEntry(Entry toModify, Language language, String wordAfterMod) {
        return switch (language) {
            case EN -> new Entry(wordAfterMod, toModify.getWordGerman(), toModify.getWordPolish());
            case DE -> new Entry(toModify.getWordEnglish(), wordAfterMod, toModify.getWordPolish());
            case PL -> new Entry(toModify.getWordEnglish(), toModify.getWordGerman(), wordAfterMod);
        };
    }

    //  ----------------- DELETE -----------------
    private boolean executeDelete(String lang, String word) {
        if (!isValidLang(lang)) return false;
        Language language = Language.valueOf(lang.toUpperCase());
        Optional<Entry> optionalWord = entryRepository.findByLanguageAndWord(language, word);

        if (entryNotFound(word, optionalWord, language)) return true;

        Entry toDelete = optionalWord.get();
        entryRepository.delete(toDelete);
        printEntryDeletedMessage(toDelete);

        return true;
    }

    //  ----------------- HELP -----------------
    private boolean executeHelp() {
        System.out.println("Commands List: ------------------------------->");
        System.out.println("help   <>  list available commands.");
        System.out.println("print   <>  print all words in csv.");
        System.out.println("add englishWord germanWord polishWord  <> add new entry to dictionary.");
        System.out.println("testme  <>  display random word from dictionary, after which you will need to write the translation in required language.");
        System.out.println("sort lang desc <>  sort by langauge. (1) parameter means language (pl, en, de). (2) means descending or ascending order (asc or desc).");
        System.out.println("search lang word  <>  search by language and word and print if record is found. (1) parameter means language (pl, en, de). (2) looks for concrete word from given language.");
        System.out.println("modify lang wordToFind wordToModify  <>  modify word with given language if record is found. (1) parameter means language (pl, en, de). (2) looks for concrete word from given language. (3) modifies to the given value.");
        System.out.println("delete lang word  <>  delete whole record with given language by word if record is found. (1) parameter means language (pl, en, de). (2) looks for concrete word from given language.");
        System.out.println("---------------------------------------------->");
        return true;
    }

    //  ----------------- PRINTING -----------------
    private void printEntryIsPresentMessage(Entry entry) {
        System.out.println("Entry [" + getStringRecordFromEntry(entry) + "] <-- was found.");

    }

    private void printEntryModifiedMessage(Entry toModify, Entry modified) {
        System.out.println("Entry [" + getStringRecordFromEntry(toModify) + "] <-- was successfully modified.");
        System.out.println("Modified to [" + getStringRecordFromEntry(modified) + "]");
        System.out.println();
    }

    private void printEntryDeletedMessage(Entry toDelete) {
        System.out.println("Entry [" + getStringRecordFromEntry(toDelete) + "] <-- was deleted.");
        System.out.println();
    }

    private void printEntryAddedMessage(Entry added) {
        System.out.println("Entry [" + getStringRecordFromEntry(added) + "] <-- was successfully added.");
        System.out.println();
    }

    private void printEntryNotUniqueMessage(Entry newWord) {
        System.out.println("Failed to add Entry ["+getStringRecordFromEntry(newWord)+"] <-- some or all meanings already exist in database.");
        System.out.println();
    }

    //  ----------------- VALIDATION -----------------
    private boolean isValidSortingType(String sortingType) {
        return Arrays.stream(SortingType.values()).anyMatch(type -> type.toString().equals(sortingType.toUpperCase()));
    }

    private boolean isValidLang(String lang) {
        return Arrays.stream(Language.values()).anyMatch(language -> language.toString().equals(lang.toUpperCase()));
    }

    private boolean entryNotFound(String word, Optional<Entry> optionalWord, Language language) {
        if (optionalWord.isEmpty()) {
            System.out.println("Word [" + word + "] from language [" + language + "] <-- was not found.");
            return true;
        }
        return false;
    }

    private boolean isUniqueValue(Entry newWord) {
        return entryRepository.findByWordEnglish(newWord.getWordEnglish()).isEmpty() &&
                entryRepository.findByWordGerman(newWord.getWordGerman()).isEmpty() &&
                entryRepository.findByWordPolish(newWord.getWordPolish()).isEmpty();
    }

    private boolean isUniqueValue(Language language, String word) {
        return switch (language) {
            case EN -> entryRepository.findByWordEnglish(word).isEmpty();
            case DE -> entryRepository.findByWordGerman(word).isEmpty();
            case PL -> entryRepository.findByWordPolish(word).isEmpty();
        };
    }
}
