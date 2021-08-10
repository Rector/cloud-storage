package ru.kir.server.core;

import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * Отслеживает изменение файлов в папке.
 * */

public class ServerWatch {
    private boolean checkIsActive = true;

    public boolean isCheckIsActive() {
        return checkIsActive;
    }

    public void setCheckIsActive(boolean checkIsActive) {
        this.checkIsActive = checkIsActive;
    }

    public void watchPackage() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("D:/JavaProject/cloud-storage/cloud-server/src/main/resources/client_files");
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            while (checkIsActive) {
                WatchKey watchKey = watchService.take();
                for (WatchEvent event : watchKey.pollEvents()) {
                    System.out.println("Event: " + event.kind() + " | fileName: " + event.context());
                }
                watchKey.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}