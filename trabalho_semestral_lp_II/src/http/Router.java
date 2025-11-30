
package com.example.uni.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.util.*;
import java.util.function.BiConsumer;

public class Router implements HttpHandler {
    static class Route {
        String method;
        String pattern; // ex: /api/courses/{id}
        List<String> parts;
        BiConsumer<HttpExchange, Map<String, String>> handler;

        Route(String method, String pattern, BiConsumer<HttpExchange, Map<String, String>> handler) {
            this.method = method;
            this.pattern = pattern;
            this.parts = Arrays.asList(pattern.split("/"));
            this.handler = handler;
        }
    }

    private final List<Route> routes = new ArrayList<>();

    public void register(String method, String pattern, BiConsumer<HttpExchange, Map<String, String>> handler) {
        routes.add(new Route(method.toUpperCase(), pattern, handler));
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod().toUpperCase();
        String path = exchange.getRequestURI().getPath();
        List<String> reqParts = Arrays.asList(path.split("/"));
        for (Route r : routes) {
            if (!r.method.equals(method)) continue;
            Map<String, String> params = new HashMap<>();
            if (match(r.parts, reqParts, params)) {
                try {
                    r.handler.accept(exchange, params);
                } catch (Exception e) {
                    HttpUtil.sendText(exchange, 500, "Erro: " + e.getMessage());
                }
                return;
            }
        }
        HttpUtil.sendText(exchange, 404, "Rota não encontrada");
    }

    private boolean match(List<String> pat, List<String> req, Map<String, String> params) {
        if (pat.size() != req.size()) return false;
        for (int i = 0; i < pat.size(); i++) {
            String pp = pat.get(i);
            String rp = req.get(i);
            if (pp.isBlank() && rp.isBlank()) continue;
            if (pp.startsWith("{") && pp.endsWith("}")) {
                String key = pp.substring(1, pp.length() - 1);
                params.put(key, rp);
            } else if (!Objects.equals(pp, rp)) {
                return false;
            }
        }
        return true;
    }
}
