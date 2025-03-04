package server.handler;

import server.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticHandler {
    public static HttpResponse handle(HttpRequest httpRequest, String documentRoot) {
        String uri = httpRequest.getUri();
        File file = new File(documentRoot, uri);

        if (!file.exists() || !file.isFile()) {
            return HttpResponseGenerator.createNotFoundResponse(); // 404 Not Found, якщо файл не знайдено
        }

        if (!file.canRead()) {
            return HttpResponseGenerator.createInternalServerErrorResponse("Немає прав для читання файлу"); // 500 Internal Server Error, якщо немає прав для читання
        }

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            String mimeType = MimeTypes.getMimeType(uri); // Визначаємо MIME-тип файлу

            HttpResponse response = new HttpResponse(HttpStatus.OK); // 200 OK
            response.setHeader("Content-Type", mimeType);
            response.setBody(fileContent);
            return response;

        } catch (IOException e) {
            e.printStackTrace();
            return HttpResponseGenerator.createInternalServerErrorResponse("Помилка читання файлу: " + e.getMessage()); // 500 Internal Server Error, якщо помилка читання файлу
        }
    }
}