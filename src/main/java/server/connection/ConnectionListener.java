package server.connection;

import server.config.ServerConfig;
import server.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

public class ConnectionListener implements Runnable {
    private final ServerSocket serverSocket;
    private final ExecutorService threadPool;
    private final ServerConfig config;
    private final Logger logger;
    private volatile boolean running = true;

    public ConnectionListener(ServerSocket serverSocket, ExecutorService threadPool, ServerConfig config, Logger logger) {
        this.serverSocket = serverSocket;
        this.threadPool = threadPool;
        this.config = config;
        this.logger = logger;
    }

    @Override
    public void run() {
        logger.info("Слухач з'єднань запущено.");
        try {
            while (running) {
                Socket socket = serverSocket.accept(); // Очікуємо нове з'єднання
                logger.debug("Отримано нове з'єднання від: " + socket.getInetAddress().getHostAddress());
                threadPool.submit(new ConnectionHandler(socket, config, logger)); // Передаємо сокет обробнику з'єднань в пул потоків
            }
        } catch (IOException e) {
            if (!serverSocket.isClosed()) {
                logger.error("Помилка при прийнятті з'єднання: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            logger.info("Слухач з'єднань зупинено.");
        }
    }

    public void stop() throws IOException {
        running = false;
        if (!serverSocket.isClosed()) {
            serverSocket.close(); // Закриваємо ServerSocket для зупинки accept()
        }
    }
}