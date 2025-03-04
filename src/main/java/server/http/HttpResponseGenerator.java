package server.http;

import server.logger.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class HttpResponseGenerator {
    private static final Logger logger = new Logger();
    private static final String SERVER_NAME = "Simple-Java-HTTP-Server/1.0";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH).withZone(ZonedDateTime.now().getZone());


    public static void generateResponse(HttpResponse httpResponse, OutputStream outputStream, String documentRoot) throws IOException {
        PrintWriter writer = new PrintWriter(outputStream);

        // Status Line
        writer.print("HTTP/1.1 " + httpResponse.getStatus().getCode() + " " + httpResponse.getStatus().getDescription() + "\r\n");

        // Заголовки за замовчуванням
        httpResponse.setHeader("Server", SERVER_NAME);
        httpResponse.setHeader("Date", DATE_FORMATTER.format(ZonedDateTime.now()));
        if (httpResponse.getBody() != null) {
            httpResponse.setHeader("Content-Length", String.valueOf(httpResponse.getBody().length));
        }

        // Записуємо заголовки
        for (Map.Entry<String, String> header : httpResponse.getHeaders().entrySet()) {
            writer.print(header.getKey() + ": " + header.getValue() + "\r\n");
        }

        // Пустий рядок, що відділяє заголовки від тіла
        writer.print("\r\n");
        writer.flush(); // Відправляємо заголовки

        // Тіло відповіді (якщо є)
        if (httpResponse.getBody() != null) {
            outputStream.write(httpResponse.getBody());
            outputStream.flush(); // Відправляємо тіло відповіді
        }
    }


    // Методи для створення стандартних HTTP-відповідей (помилок)
    public static HttpResponse createNotFoundResponse() {
        HttpResponse response = new HttpResponse(HttpStatus.NOT_FOUND);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        String body = "<!DOCTYPE html><html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1><p>Ресурс не знайдено.</p></body></html>";
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static HttpResponse createBadRequestResponse() {
        HttpResponse response = new HttpResponse(HttpStatus.BAD_REQUEST);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        String body = "<!DOCTYPE html><html><head><title>400 Bad Request</title></head><body><h1>400 Bad Request</h1><p>Некоректний запит.</p></body></html>";
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static HttpResponse createInternalServerErrorResponse(String message) {
        HttpResponse response = new HttpResponse(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        String body = "<!DOCTYPE html><html><head><title>500 Internal Server Error</title></head><body><h1>500 Internal Server Error</h1><p>Помилка сервера: " + message + "</p></body></html>";
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    public static HttpResponse createInternalServerErrorResponse() {
        return createInternalServerErrorResponse("Внутрішня помилка сервера.");
    }

    public static HttpResponse createNotImplementedResponse() {
        HttpResponse response = new HttpResponse(HttpStatus.NOT_IMPLEMENTED);
        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        String body = "<!DOCTYPE html><html><head><title>501 Not Implemented</title></head><body><h1>501 Not Implemented</h1><p>Функціональність не реалізована.</p></body></html>";
        response.setBody(body.getBytes(StandardCharsets.UTF_8));
        return response;
    }

    // Методи для відправлення стандартних відповідей помилок без HttpResponse об'єкта
    public static void sendBadRequest(OutputStream outputStream) throws IOException {
        generateResponse(createBadRequestResponse(), outputStream, null);
    }

    public static void sendInternalServerError(Socket socket) throws IOException {
        generateResponse(createInternalServerErrorResponse(), socket.getOutputStream(), null);
    }
}