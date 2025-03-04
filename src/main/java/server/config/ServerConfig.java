package server.config;

public class ServerConfig {
    private int port = 8080; // Порт за замовчуванням
    private String documentRoot = "public"; // Коренева директорія для статичних файлів за замовчуванням
    private int threadPoolSize = 10; // Розмір пулу потоків за замовчуванням

    public int getPort() {
        return port;
    }

    public String getDocumentRoot() {
        return documentRoot;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    // В майбутньому тут можна додати методи для завантаження конфігурації з файлу
}