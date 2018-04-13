package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .putParam("id", 20)
                .putParam("name", "abc")
                .putParamFrom("requestName", "r:name")
                .modal("name")
                .where("id1 = $r:id")
                .update("r:name")
                .view("hello");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .requireParamNotEmpty("name")
                .modal("name")
                .insert("name")
                .view("hello");
    }
}