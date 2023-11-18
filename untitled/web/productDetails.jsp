<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>电子商务网站</title>
    <style>
        body {
            text-align: center;
            margin: 0 auto;
        }

        .top {
            width: 75%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            text-align: center;
        }

        .middle {
            width: 75%;
            margin: 0 auto;
        }

        .container {
            width: 75%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: flex-start; /* 修改为flex-start以使标题在同一水平线上 */
            text-align: center;
        }

        .left-side {
            margin-right: 2%; /* 调整左侧和右侧之间的间距 */
            flex: 1; /* 使用flex属性使左侧占据可用空间的比例 */
        }

        .right-side {
            margin-left: 2%; /* 调整左侧和右侧之间的间距 */
            flex: 1; /* 使用flex属性使右侧占据可用空间的比例 */
        }

        .button-group {
            width: 60%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
        }

        .button-group form,
        .button-group input[type="button"],
        .button-group input[type="submit"] {
            width: 120px;
            height: 40px;

        }

        table {
            width: 75%;
            margin-top: 20px; /* 调整表格与上方内容的距离 */
            margin-bottom: 20px; /* 调整表格与下方内容的距离 */
        }

        .product-image {
            max-width: 100%;
            width: 250px;
            height: 250px;
        }

    </style>
</head>
<body>

<div class="top">
    <h3><a href="${pageContext.request.contextPath}/customer.jsp">${customer.account}</a></h3>
    <h3><a href="${pageContext.request.contextPath}/basket/list">我的购物车</a></h3>
</div>

<div class="middle">
    <hr>
</div>

<div class="container">

    <div class="left-side">
        <h1>商品展示</h1>
        <hr>
        <img src="${pageContext.request.contextPath}/images/${productDetail.picture}" alt="商品图片" class="product-image">
    </div>
    <div class="right-side">
        <h1>商品信息</h1>
        <hr>
        <table>
            <tr>
                <td>商品名称：</td>
                <td>${productDetail.name}</td>
            </tr>
            <tr>
                <td>商品价格：</td>
                <td>${productDetail.price}</td>
            </tr>
            <tr>
                <td>商品详情：</td>
                <td>${productDetail.details}</td>
            </tr>
            <tr>
                <td>存货数量：</td>
                <td>${productDetail.quantity}</td>
            </tr>
            <tr>
                <td>所属店铺：</td>
                <td>${productDetail.store}</td>
            </tr>
            <tr>
                <td>商品编号：</td>
                <td>${productDetail.id}</td>
            </tr>
            <tr>
                <td>联系商家：</td>
                <td>${contact}</td>
            </tr>
        </table>

        <div class="button-group">
            <form action="${pageContext.request.contextPath}/basket/add" method="post">
                <input type="hidden" name="AddId" value="${productDetail.id}">
                <input type="submit" value="加入购物车">
            </form>

            <input type="button" value="返回" onclick="window.history.back()"/>
        </div>

        <c:if test="${not empty Message}">
            <script>
                alert("${Message}");
            </script>
        </c:if>

    </div>
</div>
</body>
</html>