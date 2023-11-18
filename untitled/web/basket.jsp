<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

        .container {
            width: 75%;
            margin: 0 auto;
            text-align: center;
        }

        table {
            text-align: center;
        }

        .button-group {
            width: 50%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
        }

        .button-group form,
        .button-group input[type="button"],
        .button-group input[type="submit"] {
            width: 160px;
            height: 40px;

        }

        .product-image {
            max-width: 100%;
            width:80px;
            height:80px;
        }

    </style>
</head>
<body>

<div class="container">
    <h1>我的购物车</h1>
    <hr>

    <table border="1" align="center" width="75%">
        <tr>
            <th>商品图片</th>
            <th>商品名称</th>
            <th>价格</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${basketProducts}" var="product">
            <tr>
                <td><img src="${pageContext.request.contextPath}/images/${product.picture}" alt="商品图片" class="product-image"></td>
                <td>${product.name}</td>
                <td>${product.price}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/detail" method="get">
                        <input type="hidden" name="pid" value="${product.id}">
                        <input type="submit" value="查看详情">
                    </form>
                    <form action="${pageContext.request.contextPath}/basket/delete" method="get">
                        <input type="hidden" name="pid" value="${product.id}">
                        <input type="submit" value="移除商品">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <div class="button-group">
        <form action="${pageContext.request.contextPath}/pay" method="post">
            <input type="submit" value="合计${totalPrice}元，点击付款">
        </form>
        <input type="button" value="返回" onclick="window.history.back()"/>
    </div>

</div>
</body>
</html>
