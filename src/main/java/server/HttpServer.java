package server;

import server.config.ServerConfig;
import server.connection.ConnectionListener;
import server.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final ServerConfig config;
    private final Logger logger;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private ConnectionListener connectionListener;

    public HttpServer(ServerConfig config, Logger logger) {
        this.config = config;
        this.logger = logger;
    }

    public void start() throws IOException {
        logger.info("Запуск HTTP сервера на порту " + config.getPort());

        serverSocket = new ServerSocket(config.getPort()); // Відкриваємо ServerSocket на вказаному порту
        threadPool = Executors.newFixedThreadPool(config.getThreadPoolSize()); // Створюємо пул потоків

        connectionListener = new ConnectionListener(serverSocket, threadPool, config, logger); // Створюємо слухача з'єднань
        new Thread(connectionListener).start(); // Запускаємо слухача з'єднань в окремому потоці

        logger.info("Сервер запущено та очікує з'єднання...");
    }

    public void stop() throws IOException {
        logger.info("Зупинка HTTP сервера...");

        if (connectionListener != null) {
            connectionListener.stop(); // Зупиняємо слухача з'єднань
        }

        if (threadPool != null && !threadPool.isShutdown()) {
            threadPool.shutdown(); // Зупиняємо пул потоків
        }

        if (serverSocket != null && !serverSocket.isClosed()) {
            serverSocket.close(); // Закриваємо ServerSocket
        }

        logger.info("Сервер зупинено.");
    }
}