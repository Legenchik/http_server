package server.handler;

import server.http.HttpRequest;
import server.http.HttpResponse;
import server.http.HttpResponseGenerator;
import server.http.HttpStatus;

public class DynamicHandler implements RequestHandler {
    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        // Поки що не реалізовано обробку динамічного контенту
        // В майбутньому тут буде логіка для виконання серверного коду
        return HttpResponseGenerator.createNotImplementedResponse(); // 501 Not Implemented
    }

    /*public  HttpResponse handle(HttpRequest httpRequest1) {
        return new DynamicHandler().handle(httpRequest1);
    }*/
}