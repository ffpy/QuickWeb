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
        List<Integer> integerList = new ArrayList<>();
        integerList.add(0);
        integerList.add(1);
        integerList.add(2);
        QuickWeb.server(req, resp)
                .putParam("aaa", CP.of("col", "param"))
                .putParamFrom("column", "aaa.column")
                .putParamFrom("paramName", "aaa.paramName")
                .putParam("arr", new String[]{"this is str1", "this is str2"})
                .putParamFrom("str2", "arr[1]")
                .putParam("intList", integerList)
                .putParamFrom("int2", "intList[2]")
                .view("hello");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        QuickWeb.server(req, resp)
//                .requireParamNotEmpty("name")
//                .modal("name")
//                .insert("name")
                .view("hello");
    }
}