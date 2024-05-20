<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 5/17/2024
  Time: 6:21 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login Error</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }

        body, html {
            height: 100%;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(135deg, #2193b0, #6dd5ed);
        }

        .container {
            text-align: center;
            background-color: #ffffff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            padding: 20px 40px;
        }

        .error-box {
            max-width: 400px;
        }

        .error-box h1 {
            color: #2193b0;
            margin-bottom: 20px;
        }

        .error-box p {
            color: #333;
            margin-bottom: 30px;
        }

        .btn {
            text-decoration: none;
            background-color: #2193b0;
            color: #ffffff;
            padding: 10px 20px;
            border-radius: 5px;
            transition: background-color 0.3s ease;
        }

        .btn:hover {
            background-color: #1976a3;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="error-box">
            <h1>Login Failed</h1>
            <p>You must log in to access this page. If you have already logged in, please check your username and password and try again.</p>
            <a href="//localhost:8080/web/index.jsp" class="btn">Try Again</a>
        </div>
    </div>
</body>
</html>


