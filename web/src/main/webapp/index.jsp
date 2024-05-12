<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 5/10/2024
  Time: 9:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Logistics Management System - Home</title>
    <link rel="stylesheet" href="resources/css/bootstrap.css">
    <link rel="stylesheet" href="resources/css/custom.css">
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-8 offset-2">
                <img src="resources/images/shipping.svg" alt="Shipping" class="col-6 offset-3">
            </div>
            <div class="col-4 offset-4">
                <form action="login" method="post" class="d-grid">
                    <p class="fs-3 text-center">Eclipse Cargo Tracker</p>
                    <label>
                        Username
                        <input type="text" class="form-control" name="username">
                    </label>
                    <br>
                    <label>
                        Password
                        <input type="password" class="form-control" name="password">
                    </label>
                    <br>
                    <input type="submit" class="btn btn-primary" value="Login">
                </form>
            </div>
        </div>
    </div>
</body>
</html>
