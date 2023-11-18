<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>电子商务网站</title>

    <style>
        body {
            text-align: center;
        }

        .container {
            width: 75%;
            margin: 0 auto;
            display: inline-block;
            text-align: center;
        }

        .button-group {
            width: 30%;
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

        .middle {
            width: 75%;
            margin: 0 auto;
        }

        table {
            text-align: center;
        }

        .product-image {
            max-width: 100%;
            width: 80px;
            height: 80px;
        }
    </style>
</head>
<body>

<div class="container">
    <h1>我的店铺</h1>
    <hr>

    <h3>基础信息</h3>
    <div class="middle">
        <hr>
    </div>
    <form action="${pageContext.request.contextPath}/store/save" method="post">
        <table align="center">
            <tr>
                <td>店铺名称：</td>
                <td><input type="text" name="name" value="${store.name}"/></td>
            </tr>
            <tr>
                <td>店铺介绍：</td>
                <td><input type="text" name="details" value="${store.details}"/></td>
            </tr>
            <tr>
                <td>店铺联系方式：</td>
                <td><input type="text" name="contact" value="${store.contact}"/></td>
            </tr>
        </table>
        <br>
        <div class="button-group">
            <input type="submit" value="保存">
            <input type="button" value="返回" onclick="window.history.back()"/>
        </div>
    </form>

    <br><br>
    <h3>销售统计报表</h3>
    <div class="middle">
        <hr>
    </div>

    <br>
    <h4>已售商品</h4>
    <table border="1" align="center" width="70%">
        <tr>
            <th>商品图片</th>
            <th>商品名称</th>
            <th>商品价格</th>
            <th>售出数量</th>
            <th>单销售额</th>
        </tr>
        <c:forEach items="${finished}" var="order">
            <tr>
                <td><img src="${pageContext.request.contextPath}/images/${order.picture}" alt="商品图片"
                         class="product-image"></td>
                <td>${order.product}</td>
                <td>${order.price}元</td>
                <td>${order.quantity}</td>
                <td>${order.income}元</td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <h4>统计</h4>
    <table border="1" align="center" width="40%">
        <tr>
            <th>总销售额：</th>
            <th>${revenue}元</th>
        </tr>
        <tr>
            <th>总订单数：</th>
            <th>${total}</th>
        </tr>
    </table>


    <br><br>
    <h3>商品销售状态</h3>

    <div class="middle">
        <hr>
    </div>

    <br>
    <h4>在售商品</h4>
    <table border="1" align="center" width="70%">
        <tr>
            <th>商品图片</th>
            <th>商品名称</th>
            <th>商品价格</th>
            <th>商品详情</th>
            <th>存货数量</th>
        </tr>
        <c:forEach items="${onSale}" var="product">
            <tr>
                <td><img src="${pageContext.request.contextPath}/images/${product.picture}" alt="商品图片"
                         class="product-image"></td>
                <td>${product.name}</td>
                <td>${product.price}元</td>
                <td>${product.details}</td>
                <td>${product.quantity}</td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <h4>售罄商品</h4>
    <table border="1" align="center" width="70%">
        <tr>
            <th>商品图片</th>
            <th>商品名称</th>
            <th>商品价格</th>
            <th>商品详情</th>
            <th>存货数量</th>
        </tr>
        <c:forEach items="${soldOut}" var="product">
            <tr>
                <td><img src="${pageContext.request.contextPath}/images/${product.picture}" alt="商品图片"
                         class="product-image"></td>
                <td>${product.name}</td>
                <td>${product.price}元</td>
                <td>${product.details}</td>
                <td>${product.quantity}</td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <h4>用户未付款订单</h4>
    <table border="1" align="center" width="70%">
        <tr>
            <th>客户</th>
            <th>商品</th>
            <th>时间</th>
            <th>状态</th>
        </tr>
        <c:forEach items="${unpaid}" var="log">
            <tr>
                <td>${log.customer}</td>
                <td>${log.product}</td>
                <td>${log.time}</td>
                <td>${log.active}</td>
            </tr>
        </c:forEach>
    </table>

    <br>
    <h4>店铺未发货订单</h4>
    <table border="1" align="center" width="70%">
        <tr>
            <th>客户</th>
            <th>商品</th>
            <th>时间</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${notShipped}" var="log">
            <tr>
                <td>${log.customer}</td>
                <td>${log.product}</td>
                <td>${log.time}</td>
                <td>${log.active}</td>
                <td>
                    <form action="${pageContext.request.contextPath}/order" method="post">
                        <input type="hidden" name="logId" value="${log.id}">
                        <input type="hidden" name="flag" value="shipping">
                        <input type="submit" value="发货">
                    </form>
                    <form action="${pageContext.request.contextPath}/order" method="post">
                        <input type="hidden" name="logId" value="${log.id}">
                        <input type="hidden" name="flag" value="cancel">
                        <input type="submit" value="取消订单">
                    </form>
                </td>
            </tr>
        </c:forEach>
    </table>


    <br><br>
    <h3>本店铺客户的 浏览/购买 日志记录</h3>
    <div class="middle">
        <hr>
    </div>
    <table border="1" align="center" width="70%">
        <tr>
            <th>客户</th>
            <th>商品</th>
            <th>时间</th>
            <th>状态</th>
        </tr>
        <c:forEach items="${BPLogs}" var="log">
            <tr>
                <td>${log.customer}</td>
                <td>${log.product}</td>
                <td>${log.time}</td>
                <td>${log.active}</td>
            </tr>
        </c:forEach>
    </table>

    <br><br><br>

</div>


</body>
</html>
