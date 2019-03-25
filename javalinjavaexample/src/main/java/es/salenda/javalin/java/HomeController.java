package es.salenda.javalin.java;

import io.javalin.Javalin;

public class HomeController {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);
        app.get("/", ctx -> ctx.result("Hello World"));
    }
}
