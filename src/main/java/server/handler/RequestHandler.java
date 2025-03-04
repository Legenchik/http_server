package server.handler;

import server.http.HttpRequest;
import server.http.HttpResponse;

public interface RequestHandler {
    HttpResponse handle(HttpRequest httpRequest);
}