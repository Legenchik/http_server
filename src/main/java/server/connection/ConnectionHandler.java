package server.connection;

import server.config.ServerConfig;
import server.handler.RequestRouter;
import server.http.HttpRequest;
import server.http.HttpRequestParser;
import server.http.HttpResponse;
import server.http.HttpResponseGenerator;
import server.logger.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private final Socket socket;
    private final ServerConfig config;
    private final Logger logger;

    public ConnectionHandler(Socket socket, ServerConfig config, Logger logger) {
        this.socket = socket;
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void run() {
        try (InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream()) {

            logger.debug("Обробка з'єднання від: " + socket.getInetAddress().getHostAddress());

            HttpRequest httpRequest = HttpRequestParser.parse(inputStream); // Розбираємо HTTP-запит
            if (httpRequest == null) {
                // Якщо запит не вдалося розібрати (наприклад, некоректний формат), відправляємо Bad Request
                HttpResponseGenerator.sendBadRequest(outputStream);
                return;
            }

            logger.debug("Отримано запит: " + httpRequest);

            HttpResponse httpResponse = RequestRouter.route(httpRequest, config); // Маршрутизуємо запит до обробника
            HttpResponseGenerator.generateResponse(httpResponse, outputStream, config.getDocumentRoot()); // Генеруємо та відправляємо відповідь

            logger.debug("Відправлено відповідь: " + httpResponse.getStatus());

        } catch (IOException e) {
            logger.error("Помилка при обробці з'єднання: " + e.getMessage());
            e.printStackTrace();
            try {
                HttpResponseGenerator.sendInternalServerError(socket); // Відправляємо 500 Internal Server Error у випадку помилки
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                socket.close(); // Закриваємо з'єднання
                logger.debug("З'єднання закрито для: " + socket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                logger.error("Помилка при закритті з'єднання: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}