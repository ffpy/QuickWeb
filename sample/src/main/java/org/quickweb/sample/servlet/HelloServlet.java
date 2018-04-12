package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;
import org.quickweb.session.EditableParamScope;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .putParam("id", 2, EditableParamScope.APPLICATION)
                .modal("name")
                .where("id = $an:id")
                .update("name")
                .view("hello");
    }
}