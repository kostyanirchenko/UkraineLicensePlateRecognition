package util;

import java.io.File;

/**
 * Created by NKostya on 24.05.2016.
 */
public class FileUtil {

    public static String getFileExtension(File file) {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else return "";
    }
}
