<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>注册</title>
</head>
<body>
    <form action="register" method="post">
        <div>
            <label for="name">昵称</label>
            <input id="name" name="name">
        </div>
        <div>
            <label for="username">账号</label>
            <input id="username" name="username">
        </div>
        <div>
            <label for="password">密码</label>
            <input id="password" name="password" type="password">
        </div>
        <button type="submit">确定注册</button>
        <button type="reset">重置</button>
    </form>
</body>
</html>
