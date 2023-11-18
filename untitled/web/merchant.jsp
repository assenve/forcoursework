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
        if (window.confirm('请注意，该操作将彻底删除该账号，一旦继续将丢失与该账号相关的全部数据、与该账号相联店铺的全部数据，请谨慎确认是否删除！！！')) {
            document.location.href = "${pageContext.request.contextPath}/quit?id=" + id + "&role=merchant";
        }
    }
</script>

<div class="container">
    <h1>个人信息</h1>
    <hr>
    <table align="center" width="30%">
        <tr>
            <td>账号：</td>
            <td>${merchant.account}</td>
        </tr>
        <tr>
            <td>密码：</td>
            <td>${merchant.password}</td>
        </tr>
        <tr>
            <td>邮箱：</td>
            <td>${merchant.email}</td>
        </tr>
        <tr>
            <td>店铺：</td>
            <td>${store.name}</td>
        </tr>
    </table>

    <div class="button-group">
        <form action="${pageContext.request.contextPath}/logout" method="post">
            <input type="submit" value="登出">
        </form>

        <input type="button" onclick="del(${merchant.id})" value="注销">

        <form action="${pageContext.request.contextPath}/store.jsp" method="post">
            <input type="submit" value="返回">
        </form>
    </div>

</div>
</body>
</html>
