package server.handler;

import server.http.HttpRequest;
import server.http.HttpResponse;
import server.http.HttpStatus;
import server.logger.Logger;

import java.nio.charset.StandardCharsets;

public class PostHandler  {
    private static final Logger logger = new Logger();


    public static HttpResponse handle(HttpRequest httpRequest) {
        if (httpRequest.getBody() != null && httpRequest.getBody().length > 0) {
            String requestBody = new String(httpRequest.getBody(), StandardCharsets.UTF_8);
            logger.info("Отримано POST запит з тілом: " + requestBody);

            // Тут можна додати логіку обробки отриманих даних
            // Наприклад, збереження в базу даних, обробка форми, тощо.

            // Повертаємо успішну відповідь 200 OK
            HttpResponse response = new HttpResponse(HttpStatus.OK);
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            response.setBody(("<!DOCTYPE html><html><head><title>Успіх!</title></head><body><h1>Дані отримано</h1><p>Сервер успішно отримав ваші дані: " + requestBody + "</p></body></html>".getBytes(StandardCharsets.UTF_8)).getBytes());
            return response;
        } else {
            // Якщо в POST-запиті немає тіла
            HttpResponse response = new HttpResponse(HttpStatus.BAD_REQUEST);
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            response.setBody("<!DOCTYPE html><html><head><title>Помилка</title></head><body><h1>Помилка запиту</h1><p>У POST-запиті відсутнє тіло.</p></body></html>".getBytes(StandardCharsets.UTF_8));
            return response;
        }
    }


}