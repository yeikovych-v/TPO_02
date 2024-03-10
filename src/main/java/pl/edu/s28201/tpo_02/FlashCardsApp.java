package pl.edu.s28201.tpo_02;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.edu.s28201.tpo_02.controller.FlashCardsController;
import pl.edu.s28201.tpo_02.service.FileService;

import java.io.File;

@SpringBootApplication
public class FlashCardsApp {

    private final FileService fileService;
    private final FlashCardsController controller;
    private final ApplicationContext appContext;
    @Value("${pl.edu.pja.tpo02.filename}")
    private String dictionaryPath;
    @Autowired
    public FlashCardsApp(FileService fileService, FlashCardsController controller, ApplicationContext appContext) {
        this.fileService = fileService;
        this.controller = controller;
        this.appContext = appContext;
    }

    public static void main(String[] args) {
        SpringApplication.run(FlashCardsApp.class, args);
    }

    public void initiateShutdown() {
        SpringApplication.exit(appContext, () -> 0);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            fileService.readFromCsv(new File(dictionaryPath));
            controller.listenToCommands();
            initiateShutdown();
        };
    }
}
