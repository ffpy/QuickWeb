package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;
import org.quickweb.session.scope.EditableScope;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .view("login");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .onError((e, quickSession) -> {
                    quickSession.view("login");
                    System.out.println("error: " + e);
                })
                .clearParams(EditableScope.SESSION)
                .requireParamNotEmpty("r:username", "r:password")
                .modal("user").select("password, name").where("username = $r:username").findFirst("m:user")
                .requireParamNotEmpty(new Exception("user not found"), "m:user")
                .requireParamEqualsWith("r:password", "m:user.password")
                .setParam("s:name", "m:user.name")
                .redirect("/");

    }
}