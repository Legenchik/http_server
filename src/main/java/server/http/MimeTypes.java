package server.http;

import java.util.HashMap;
import java.util.Map;

public class MimeTypes {
    private static final Map<String, String> mimeTypesMap = new HashMap<>();

    static {
        mimeTypesMap.put("html", "text/html");
        mimeTypesMap.put("css", "text/css");
        mimeTypesMap.put("js", "application/javascript");
        mimeTypesMap.put("jpg", "image/jpeg");
        mimeTypesMap.put("jpeg", "image/jpeg");
        mimeTypesMap.put("png", "image/png");
        mimeTypesMap.put("gif", "image/gif");
        mimeTypesMap.put("ico", "image/x-icon");
        mimeTypesMap.put("txt", "text/plain"); // Додано для текстових файлів
        mimeTypesMap.put("plain", "text/plain"); // Альтернативне розширення для текстових файлів
    }

    public static String getMimeType(String filename) {
        String extension = getFileExtension(filename);
        return mimeTypesMap.getOrDefault(extension, "application/octet-stream"); // За замовчуванням - application/octet-stream для невідомих типів
    }

    private static String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            return ""; // Немає розширення або крапка в кінці
        }
        return filename.substring(dotIndex + 1).toLowerCase(); // Розширення в нижньому регістрі
    }
}