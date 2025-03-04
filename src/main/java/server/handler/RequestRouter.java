package server.handler;

import server.config.ServerConfig;
import server.http.HttpRequest;
import server.http.HttpResponse;
import server.http.HttpStatus;

public class RequestRouter {
    public static HttpResponse route(HttpRequest httpRequest, ServerConfig config) {
        String uri = httpRequest.getUri();

        if (uri.equals("/")) {
            uri = "/index.html"; // За замовчуванням віддаємо index.html для кореневого URI
        }

        if (isStaticResource(uri)) {
            return StaticHandler.handle(httpRequest, config.getDocumentRoot()); // Обробка статичних файлів
        } else {
            DynamicHandler dh = new DynamicHandler();
            return dh.handle(httpRequest); // Обробка динамічного контенту (поки що заглушка)
        }
    }

    private static boolean isStaticResource(String uri) {
        String lowerUri = uri.toLowerCase();
        return lowerUri.endsWith(".html") || lowerUri.endsWith(".css") || lowerUri.endsWith(".js") ||
                lowerUri.endsWith(".jpg") || lowerUri.endsWith(".jpeg") || lowerUri.endsWith(".png") ||
                lowerUri.endsWith(".gif") || lowerUri.endsWith(".ico");
    }
}