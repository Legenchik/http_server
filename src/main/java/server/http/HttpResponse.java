package server.http;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpStatus status;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(HttpStatus status) {
        this.status = status;
    }

    // Геттери та сеттери для всіх полів
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }


    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "status=" + status +
                ", headers=" + headers +
                ", body length=" + (body == null ? 0 : body.length) +
                '}';
    }
}