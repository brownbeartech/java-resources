package tech.brownbear.resources;

import java.io.File;
import java.io.IOException;

public class PathHelper {
    public static String getRootPath() {
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException e) {

        }
        return System.getProperty("user.dir");
    }
}
