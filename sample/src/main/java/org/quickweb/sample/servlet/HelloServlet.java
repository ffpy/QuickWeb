package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;
import org.quickweb.session.param.CP;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .requireParamNotEmpty("r:name")
                .modal("name")
                .find("names")
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