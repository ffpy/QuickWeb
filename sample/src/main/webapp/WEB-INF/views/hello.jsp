<%@ page import="java.util.Enumeration" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%
        Enumeration<String> names = request.getAttributeNames();
        PrintWriter writer = response.getWriter();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            writer.print(name + ": " + request.getAttribute(name) + "<br>");
        }
    %>
</body>
</html>
