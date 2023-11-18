<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>电子商务网站</title>
    <base href="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${pageContext.request.contextPath}/">

    <style>
        body {
            width: 75%;
            margin: 0 auto;
            text-align: center;
        }

        .top {
            width: 75%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            text-align: center;
        }

        table {
            text-align: center;
        }

        .middle {
            width: 74%;
            margin: 0 auto;
        }

        .product-image {
            max-width: 100%;
            width:80px;
            height:80px;
        }

    </style>
</head>
<body>

<div class="top">
    <h3><a href="customer/details">我的账户</a></h3>
    <h3><a href="basket/list">我的购物车</a></h3>
</div>
<hr>
<h1>商品列表</h1>
<div class="middle">
    <hr>
</div>
<table border="1" align="center" width="75%">
    <tr>
        <th>商品图片</th>
        <th>商品名称</th>
        <th>商品价格</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${products}" var="product">
        <tr>
            <td><img src="${pageContext.request.contextPath}/images/${product.picture}" alt="商品图片" class="product-image"></td>
            <td>${product.name}</td>
            <td>${product.price}元</td>
            <td>
                <form action="${pageContext.request.contextPath}/detail" method="get">
                    <input type="hidden" name="pid" value="${product.id}">
                    <input type="hidden" name="sid" value="${product.store}">
                    <input type="submit" value="查看详情">
                </form>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>