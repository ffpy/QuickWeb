package org.quickweb.sample.servlet;

import org.quickweb.QuickWeb;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

public class RegisterServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp).view("register");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
                .requireParamNotEmpty("name", "username", "password")
                .setParam("ctime", new Timestamp(Calendar.getInstance().getTimeInMillis()))
                .modal("user")
                .insert("name", "username", "password", "ctime")
                .redirect("/login");
    }
}
