package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;
import org.quickweb.modal.ResultType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .startTransaction()
                .modal("name")
                .avg("avg", "id")
                .modal("name")
                .count("count")
                .modal("name")
                .max("max", "id", ResultType.INT)
                .modal("name")
                .sum("sum", "id", ResultType.DOUBLE)
                .modal("name")
                .max("maxBirth", "birth", ResultType.DATETIME)
                .modal("name")
                .max("maxName", "name", ResultType.STRING)
                .endTransaction()
                .view("hello");
    }
}