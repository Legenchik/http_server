package server.http;

import server.logger.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HttpRequestParser {
    private static final Logger logger = new Logger();

    public static HttpRequest parse(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        HttpRequest httpRequest = new HttpRequest();

        try {
            // Читаємо Request Line (Метод URI Версія)
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                return null; // Пустий запит або обрив з'єднання
            }
            String[] requestLineParts = requestLine.split(" ");
            if (requestLineParts.length != 3) {
                logger.warn("Некоректний Request Line: " + requestLine);
                return null; // Некоректний Request Line
            }

            try {
                httpRequest.setMethod(HttpMethod.valueOf(requestLineParts[0])); // Метод запиту
            } catch (IllegalArgumentException e) {
                logger.warn("Невідомий HTTP метод: " + requestLineParts[0]);
                httpRequest.setMethod(HttpMethod.GET); // За замовчуванням GET, якщо не вдалося розібрати
            }

            httpRequest.setUri(requestLineParts[1]); // URI
            httpRequest.setHttpVersion(requestLineParts[2]); // Версія HTTP


            // Читаємо заголовки
            String headerLine;
            while ((headerLine = reader.readLine()) != null && !headerLine.isEmpty()) {
                String[] headerParts = headerLine.split(": ", 2); // Розділяємо за першим ": "
                if (headerParts.length == 2) {
                    httpRequest.getHeaders().put(headerParts[0], headerParts[1]); // Додаємо заголовок
                } else {
                    logger.warn("Некоректний заголовок: " + headerLine);
                }
            }

            // Обробка тіла запиту (поки що не реалізовано для GET)
            if (httpRequest.getMethod() == HttpMethod.POST || httpRequest.getMethod() == HttpMethod.PUT) {
                int contentLength = 0;
                if (httpRequest.getHeaders().containsKey("Content-Length")) {
                    try {
                        contentLength = Integer.parseInt(httpRequest.getHeaders().get("Content-Length"));
                    } catch (NumberFormatException e) {
                        logger.warn("Некоректний Content-Length: " + httpRequest.getHeaders().get("Content-Length"));
                        contentLength = 0; // Ігноруємо некоректний Content-Length
                    }
                }

                if (contentLength > 0) {
                    char[] bodyChars = new char[contentLength];
                    reader.read(bodyChars, 0, contentLength);
                    httpRequest.setBody(new String(bodyChars).getBytes(StandardCharsets.UTF_8)); // Читаємо тіло запиту
                }
            }


            return httpRequest;

        } catch (IOException e) {
            logger.error("Помилка при розборі HTTP-запиту: " + e.getMessage());
            e.printStackTrace();
            return null; // Помилка розбору запиту
        }
    }
}