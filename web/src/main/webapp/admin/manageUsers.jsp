<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 5/11/2024
  Time: 10:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Track Cargo</title>
    <link rel="stylesheet" href="../resources/fontawesome-free-6.5.2-web/css/fontawesome.min.css">
    <link rel="stylesheet" href="../resources/fontawesome-free-6.5.2-web/css/solid.min.css">
    <link rel="stylesheet" href="../resources/css/bootstrap.css">
    <link rel="stylesheet" href="../resources/css/custom.css">
</head>
<body>
    <div class="container-fluid vh-100">
        <div class="row">

            <%-- Navbar - start --%>
            <div class="col-2 sidebar">
                <div class="row">
                    <div class="col-12 vh-100 bg-color-dark text-color-light">
                        <div class="row py-3">
                            <div class="col-10 offset-1 px-2 py-2 nav-header" style="user-select: none;">
                                <i class="fa-solid fa-ship"></i>
                                Wave Logistics
                            </div>
                            <hr style="width: 90%; margin-left: 5%;" class="mb-4">
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='dashboard.jsp';">
                                <i class="fa-solid fa-house-laptop"></i>
                                Dashboard
                            </div>
                            <c:if test="${pageContext.request.isUserInRole('supervisor')}">
                                <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageCargo.jsp';">
                                    <i class="fa-solid fa-cart-flatbed"></i>
                                    Cargo
                                </div>
                                <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageRoute.jsp';">
                                    <i class="fa-solid fa-route"></i>
                                    Book & Edit Route
                                </div>
                            </c:if>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='trackCargo.jsp';">
                                <i class="fa-solid fa-magnifying-glass-location"></i>
                                Track Cargo
                            </div>
                            <c:if test="${pageContext.request.isUserInRole('admin')}">
                                <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item selected">
                                    <i class="fa-solid fa-user-group"></i>
                                    Users
                                </div>
                            </c:if>
                            <hr style="width: 90%; margin-left: 5%;">
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item nav-logout" onclick="logout();">
                                <i class="fa-solid fa-right-from-bracket"></i>
                                Sign Out
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%-- Navbar - end --%>

            <%-- Main - start --%>
            <div class="col-10 py-2 px-3">

                <%-- User Creation Container - start --%>
                <h3>Register New User</h3>
                <div class="row px-3 mb-5">
                    <div class="col-8 rounded-3 container-shadow px-2 py-3">
                        <div class="row">
                            <div class="col-6">
                                <label for="username" class="fw-bold">Username</label>
                                <input type="text" id="username" class="form-control">
                            </div>
                            <div class="col-6">
                                <label for="password" class="fw-bold">Password</label><br>
                                <input type="password" id="password" class="form-control">
                            </div>
                            <div class="col-6">
                                <label for="usertype" class="fw-bold">Select User Type</label><br>
                                <select id="usertype" class="form-select">
                                    <option value="-1">Select</option>
                                    <option value="0">Admin</option>
                                    <option value="1">Supervisor</option>
                                    <option value="2">User</option>
                                </select>
                            </div>
                            <div class="col-12 mt-3 text-end">
                                <button class="btn btn-danger col-auto align-self-end" onclick="registerUser();">Register</button>
                            </div>
                        </div>
                    </div>
                </div>
                <%-- User Creation Container - end --%>

            </div>
            <%-- Main - end --%>
        </div>
    </div>

    <script src="../resources/js/alert.js"></script>
    <script src="../resources/js/bootstrap.bundle.js"></script>
    <script src="../resources/js/custom.js"></script>
</body>
</html>
