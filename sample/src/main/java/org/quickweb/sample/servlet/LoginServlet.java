package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp).view("login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .onError((e, quickSession) -> {
                    quickSession.view("login");
                    System.out.println("error: " + e);
                })
                .invalidateSession()
                .requireParamNotEmpty("username", "password")
                .modal("user")
                .select("password, name")
                .where("username = $username")
                .findFirst("user")
                .watch(session -> {
                    Map<String, Object> user = session.getParam("user");
                    session.putParam("m:password", user.get("password"));
                    session.putParam("m:name", user.get("name"));
                })
                .requireParamEqualsWith("r:password", "m:password")
                .setSessionFrom("name", "name")
                .viewPath("/");
    }
}
