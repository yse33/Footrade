package com.example.footrade.schedule;

import com.example.footrade.service.NotificationService;
import com.example.footrade.service.ShoeService;
import com.example.footrade.service.UserService;
import com.google.firebase.FirebaseApp;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScraperScheduler {
    private final NotificationService notificationService;
    private final ShoeService shoeService;
    private final UserService userService;

    private final String title = "New Shoes Available";
    private final String message = "Shoes from your favorite's list are on sale! Check them out now!";

    @Scheduled(fixedRate = 120000)
    public void runScraper() {
        try {
            Date date = new Date();

            ProcessBuilder processBuilder = new ProcessBuilder("python", "main.py");
            File workingDirectory = new File("C:/Users/yasen/Desktop/Footrade/scraper_scripts");
            processBuilder.directory(workingDirectory);
            processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("SCRAPER: Scraper ran and executed successfully");
            } else {
                System.out.println("SCRAPER: Scraper failed during execution");
            }

            List<ObjectId> updatedShoeIds = shoeService.getUpdatedShoeIdsAfter(date);
            System.out.println(updatedShoeIds);
            List<String> userDeviceTokens = userService.getDeviceTokensByShoeIds(updatedShoeIds);
            System.out.println(userDeviceTokens);
            for (String deviceToken : userDeviceTokens) {
                notificationService.sendNotification(title, message, deviceToken);
                System.out.println("SCRAPER: Notification sent to " + deviceToken);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("SCRAPER: Scraper failed to run");
        }
    }

//    @Scheduled(fixedRate = 60000)
//    public void sendDummyNotification() {
//        notificationService.sendNotification(title, message, "cnB_92VTSA-Zw0VoVbQY1q:APA91bHsB71bcuMrovzdlhTjxopNFrIdWDlMQvXOkjV_QZsuMs_JmNo9Is5zNSRxL0m62OybUl7H0k2Rjg9Mw_iF29YcvsPlvvEkp5HdmoPqzcPgyV1g6YkDNe3Y-yo7RTp8PehE2KJ5");
//        System.out.println("Dummy notification sent");
//    }
}
