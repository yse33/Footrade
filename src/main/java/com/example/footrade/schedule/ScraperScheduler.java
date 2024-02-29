package com.example.footrade.schedule;

import com.example.footrade.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScraperScheduler {
    private final NotificationService notificationService;

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void runScraper() {
        try {
            Date date = new Date();

            ProcessBuilder processBuilder = new ProcessBuilder("python", "dummy.py");
            File workingDirectory = new File("scraper");
            processBuilder.directory(workingDirectory);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("SCRAPER: Scraper ran and executed successfully");
            } else {
                System.out.println("SCRAPER: Scraper failed during execution");
                return;
            }

            String title = "New Shoes Available";
            String message = "Shoes from your favorite's list are on sale! Check them out now!";
            notificationService.sendNotificationForUpdatedShoes(title, message, date);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("SCRAPER: Scraper failed to run");
        }
    }
}
