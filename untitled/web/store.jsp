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

        .button-group {
            width: 75%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
        }

        .button-group.center-align {
            display: flex;
            justify-content: center;
        }

        .button-group form,
        .button-group input[type="button"],
        .button-group input[type="submit"] {
            width: 120px;
            height: 40px;

        }

    </style>
</head>
<body>

<script type="text/javascript">
    function del(id) {
        if (window.confirm('请注意，该操作将下架此商品，请再次确认是否下架！！！')) {
            document.location.href = "${pageContext.request.contextPath}/product/delete?pid=" + id;
        }
    }
</script>

<div class="top">
    <h3><a href="merchant.jsp">个人信息</a></h3>
    <h3><a href="store/details">我的店铺</a></h3>
</div>
<hr>


<h1>店铺商品管理</h1>
<div class="button-group center-align">
    <form action="${pageContext.request.contextPath}/add.jsp" method="post">
        <input type="submit" value="上架商品">
    </form>
</div>
<div class="middle">
    <hr>
</div>
<table border="1" align="center" width="75%">
    <tr>
        <th>商品图片</th>
        <th>商品名称</th>
        <th>商品价格</th>
        <th>商品详情</th>
        <th>存货数量</th>
        <th>操作</th>
    </tr>
    <c:forEach items="${products}" var="product">
        <tr>
            <td><img src="${pageContext.request.contextPath}/images/${product.picture}" alt="商品图片" class="product-image"></td>
            <td>${product.name}</td>
            <td>${product.price}元</td>
            <td>${product.details}</td>
            <td>${product.quantity}</td>
            <td>

                <form action="${pageContext.request.contextPath}/modify" method="get">
                    <input type="hidden" name="pid" value="${product.id}">
                    <input type="submit" value="修改">
                </form>

                <input type="button" onclick="del(${product.id})" value="下架">
            </td>
        </tr>
    </c:forEach>
</table>


<c:if test="${not empty Message}">
    <script>
        alert("${Message}");
    </script>
</c:if>

</body>
</html>