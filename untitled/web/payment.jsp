<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>电子商务网站</title>
    <style>
        body {
            width: 75%;
            margin: 0 auto;
            text-align: center;
        }

        .button-group input[type="submit"] {
            margin: 0 auto;
            width: 120px;
            height: 40px;
        }
    </style>
</head>
<body>
<h1>付款成功！</h1>
<hr>
当商家发货后，将发送邮件到您的邮箱${customer.email}...
<div class="button-group">
    <form action="${pageContext.request.contextPath}/product.jsp" method="post">
        <input type="submit" value="返回主页面">
    </form>
</div>
</body>
</html>
