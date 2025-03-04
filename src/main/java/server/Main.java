package server;

import server.config.ServerConfig;
import server.logger.Logger;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ServerConfig config = new ServerConfig(); // Завантажуємо конфігурацію сервера (зараз дефолтну)
        Logger logger = new Logger(); // Ініціалізуємо логер

        try {
            HttpServer server = new HttpServer(config, logger);
            server.start(); // Запускаємо сервер
        } catch (IOException e) {
            logger.error("Помилка при запуску сервера: " + e.getMessage());
            e.printStackTrace();
        }
    }
}