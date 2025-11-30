
package com.example.uni;

import com.example.uni.controller.CourseController;
import com.example.uni.controller.LessonController;
import com.example.uni.db.Database;
import com.example.uni.http.Router;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        // Inicializa DB e schema
        Database.initSchema();

        // Router e Controllers
        Router router = new Router();
        CourseController cc = new CourseController();
        LessonController lc = new LessonController();

        // Rotas de Course
        router.register("GET",  "/api/courses",           cc::list);
        router.register("GET",  "/api/courses/{id}",      cc::get);
        router.register("POST", "/api/courses",           cc::create);
        router.register("PUT",  "/api/courses/{id}",      cc::update);
        router.register("DELETE","/api/courses/{id}",     cc::delete);

        // Rotas de Lesson
        router.register("GET",  "/api/lessons",           lc::list);
        router.register("GET",  "/api/lessons/{id}",      lc::get);
        router.register("POST", "/api/lessons",           lc::create);
        router.register("PUT",  "/api/lessons/{id}",      lc::update);
        router.register("DELETE","/api/lessons/{id}",     lc::delete);

        // Rotas 1..N (lessons sob course)
        router.register("GET",  "/api/courses/{courseId}/lessons",  lc::listByCourse);
        router.register("POST", "/api/courses/{courseId}/lessons",  lc::createUnderCourse);

        // HTTP Server
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", router);
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(8));
        server.start();
        System.out.println("Server rodando em http://localhost:8080");
    }
}
