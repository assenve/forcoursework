<%@page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
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
        if (window.confirm('请注意，该操作将在删除提交的数据的同时删除您注册的账号，请谨慎确认是否删除！！！')) {
            document.location.href = "${pageContext.request.contextPath}/quit?id=" + id + "&role=merchant";
        }
    }
</script>


<h1>创建店铺</h1>
<h3 align="center">欢迎，这里开始创建您的店铺</h3>
<h6 align="center">带*号的为必填项</h6>

<hr>
<form action="${pageContext.request.contextPath}/creat/save" method="post" width="75%" align="center">
    <table align="center">
        <tr>
            <td>店铺名称*：</td>
            <td><input type="text" name="name"/></td>
        </tr>
        <tr>
            <td>店铺描述：</td>
            <td><input type="text" name="details"/></td>
        </tr>
        <tr>
            <td>店铺联系方式：</td>
            <td><input type="text" name="contact"/></td>
        </tr>

    </table>
    <div class="button-group">
        <input type="submit" value="注册"/>
        <input type="button" value="取消" onclick="del(${merchant.id})"/>
    </div>


</form>

</body>
</html>