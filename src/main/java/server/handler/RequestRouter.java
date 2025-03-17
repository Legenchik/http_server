package server.handler;

import server.config.ServerConfig;
import server.http.*;

public class RequestRouter {
    public static HttpResponse route(HttpRequest httpRequest, ServerConfig config) {
        String uri = httpRequest.getUri();
        HttpMethod method = httpRequest.getMethod();

        if (uri.equals("/")) {
            uri = "/index.html"; // За замовчуванням віддаємо index.html для кореневого URI
        }

        if (method == HttpMethod.GET) {
            if (isStaticResource(uri)) {
                return StaticHandler.handle(httpRequest, config.getDocumentRoot()); // Обробка статичних файлів для GET
            } else {
                DynamicHandler dh = new DynamicHandler();
                return dh.handle(httpRequest); // Обробка динамічного контенту (поки що заглушка) для GET
            }
        } else if (method == HttpMethod.POST) {
            return PostHandler.handle(httpRequest); // Обробка POST запитів
        } else {
            return HttpResponseGenerator.createNotImplementedResponse(); // Для інших методів повертаємо 501 Not Implemented
        }
    }

    private static boolean isStaticResource(String uri) {
        String lowerUri = uri.toLowerCase();
        return lowerUri.endsWith(".html") || lowerUri.endsWith(".css") || lowerUri.endsWith(".js") ||
                lowerUri.endsWith(".jpg") || lowerUri.endsWith(".jpeg") || lowerUri.endsWith(".png") ||
                lowerUri.endsWith(".gif") || lowerUri.endsWith(".ico");
    }
}