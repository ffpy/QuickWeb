<%@ page import="java.util.Enumeration" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <form action="hello" method="post">
        <label for="name">Name</label>
        <input id="name" name="name">
        <button type="submit">提交</button>
    </form>

    <%
        Enumeration<String> names = request.getAttributeNames();
        PrintWriter writer = response.getWriter();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            Object o = request.getAttribute(name);
            writer.print(name + ": " + o + " [" + o.getClass() + "]<br>");
        }
    %>
</body>
</html>
