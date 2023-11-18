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


        table {
            margin-top: 20px; /* 调整表格与上方内容的距离 */
            margin-bottom: 20px; /* 调整表格与下方内容的距离 */
        }

        td {
            text-align: center;
            font-size: 20px;
        }

        .button {
            width: 80px;
            height: 30px;
        }

        .button-group {
            width: 40%;
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

    </style>

</head>
<body>

<script type="text/javascript">
    function del(id) {
        if (window.confirm('请注意，该操作将彻底删除该账号，一旦继续将丢失全部与该账号相关的数据，请谨慎确认是否删除！！！')) {
            document.location.href = "${pageContext.request.contextPath}/quit?id=" + id + "&role=customer";
        }
    }
</script>

<div class="container">
    <h1>个人信息</h1>
    <hr>

    <form action="${pageContext.request.contextPath}/customer/save" method="post">
        <table align="center" width="30%">
            <tr>
                <td>账号：</td>
                <td>${customer.account}</td>
            </tr>
            <tr>
                <td>密码：</td>
                <td><input type="text" name="password" value="${customer.password}"/></td>
            </tr>
            <tr>
                <td>邮箱：</td>
                <td><input type="text" name="email" value="${customer.email}"/></td>
            </tr>
        </table>
        <input type="submit" value="保存" class="button">

    </form>

    <div class="button-group">
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <input type="submit" value="登出">
        </form>

        <input type="button" onclick="del(${customer.id})" value="注销">

        <form action="${pageContext.request.contextPath}/product.jsp" method="post">
            <input type="submit" value="返回">
        </form>
    </div>

    <c:if test="${not empty Message}">
        <script>
            alert("${Message}");
        </script>
    </c:if>

</div>
</body>
</html>
