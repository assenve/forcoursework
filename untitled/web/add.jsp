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
<h1>${store.name}</h1>
<h3 align="center">上架商品</h3>
<h6 align="center">带*号的为必填项</h6>
<hr>
<form action="${pageContext.request.contextPath}/product/save" method="post" enctype="multipart/form-data" width="75%"
      align="center">
    <table align="center">
        <tr>
            <td>商品名称*：</td>
            <td><input type="text" name="name"/></td>
        </tr>
        <tr>
            <td>商品价格*：</td>
            <td><input type="text" name="price"/></td>
        </tr>
        <tr>
            <td>商品详情：</td>
            <td><input type="text" name="details"/></td>
        </tr>
        <tr>
            <td>商品数量*：</td>
            <td><input type="text" name="quantity"/></td>
        </tr>
        <tr>
            <td>商品图片上传</td>
            <td> <input type="file" name="picture"/></td>
        </tr>
    </table>
<div class="button-group">
    <input type="submit" value="上架"/>
    <input type="button" value="返回" onclick="window.history.back()"/>
</div>

</form>

</body>
</html>