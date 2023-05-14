package updater;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Updater
 * DB 업데이트용 클래스
 */

public class Updater {
    public Updater() {
        try {
            InputStream inputStream = new FileInputStream("src/main/resources/config/updaterConfig.yml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
