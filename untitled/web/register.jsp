<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>电子商务网站</title>
    <style>
        body {
            text-align: center;
        }

        .container {
            width: 40%;
            margin: 0 auto;
            display: inline-block;
            text-align: center;
        }

        .form-group {
            margin-bottom: 10px;
        }

        .checkbox-label {
            display: inline-block;
            vertical-align: middle;
            margin-right: 10px;
            font-size: 14px; /* 修改字体大小为 14px */
        }

        .button-group {
            margin-top: 14px; /* 调整按钮组的上边距 */
        }

        .button-group input[type="submit"] {
            font-size: 16px; /* 修改登录按钮的字体大小为 16px */
            padding: 4px 36px; /* 修改登录按钮的内边距 */
        }

        .register-link {
            margin-top: 10px; /* 调整注册链接的上边距 */
            font-size: 12px; /* 修改注册链接的字体大小为 12px */
        }

    </style>
</head>
<body>
<div class="container">
    <h1>新用户注册</h1>
    <hr>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <div class="form-group">
            <label for="account">账号:</label>
            <input type="text" name="account" id="account">
        </div>

        <div class="form-group">
            <label for="password">密码:</label>
            <input type="password" name="password" id="password">
        </div>

        <div class="form-group">
            <label for="email">邮箱:</label>
            <input type="text" name="email" id="email">
        </div>

        <div class="form-group">
            <input type="radio" name="role" id="customer" value="customer" checked>
            <label class="checkbox-label" for="customer">顾客</label>
            <input type="radio" name="role" id="merchant" value="merchant">
            <label class="checkbox-label" for="merchant">商家</label>
        </div>

        <div class="button-group">
            <input type="submit" value="注册">
        </div>
    </form>
    <a href="${pageContext.request.contextPath}/index.jsp" class="register-link">已有账户？点我登录</a>

    <c:if test="${not empty Message}">
        <script>
            alert("${Message}");
        </script>
    </c:if>
</div>

</body>
</html>