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
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item selected">
                                <i class="fa-solid fa-magnifying-glass-location"></i>
                                Track Cargo
                            </div>
                            <c:if test="${pageContext.request.isUserInRole('admin')}">
                                <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageUsers.jsp';">
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

                <%-- Cargo Tracking Container - start --%>
                <h3>Track Cargo</h3>
                <div class="row px-3 mb-5">
                    <div class="col-6 rounded-3 container-shadow px-2 py-3">
                        <div class="row">
                            <div class="col-12">
                                <label for="tracking_id" class="fw-bold">Tracking ID</label><br>
                                <span style="font-size: 0.9rem;" class="fw-light">Enter the tracking ID</span>
                                <div class="row">
                                    <div class="col-8">
                                        <input type="text" class="form-control" id="tracking_id">
                                    </div>
                                    <div class="col-4">
                                        <button class="btn btn-danger align-self-end" onclick="trackCargo();">Track Now</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <%-- Cargo Tracking Container - end --%>

                <%-- Tracking Status - start --%>
                <div class="row d-none" id="result_container">
                    <div class="col-12">

                        <div class="row">
                            <div class="col-12 my-2">
                                <div class="row">
                                    <div class="col-4 offset-4 rounded-3 bg-danger text-light py-4 px-3 text-center d-none" id="status_unknown">
                                        <span class="fs-3"><i class="fa-solid fa-triangle-exclamation"></i></span><br>
                                        <span class="fs-4">Location Unknown</span><br>
                                        <span class="fw-lighter">Cannot find location</span>
                                    </div>
                                    <div class="col-4 offset-4 rounded-3 bg-primary text-light py-4 px-3 text-center d-none" id="status_in_transit">
                                        <span class="fs-3"><i class="fa-solid fa-ferry"></i></span><br>
                                        <span class="fs-4">In Transit</span><br>
                                        <span class="fw-lighter">Ship in the waters</span>
                                    </div>
                                    <div class="col-4 offset-4 rounded-3 bg-success text-light py-4 px-3 text-center d-none" id="status_in_port">
                                        <span class="fs-3"><i class="fa-solid fa-anchor"></i></span><br>
                                        <span class="fs-4">In Port</span><br>
                                        <span class="fw-lighter">Current Location is: <span id="current_location"></span></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-12">
                                <div class="d-flex flex-row justify-content-between align-items-center align-content-center" id="route_line_container">

                                </div>

                                <div class="d-flex flex-row justify-content-between align-items-center" id="route_terminal_container">

                                </div>
                            </div>
                        </div>

                    </div>
                </div>
                <%-- Tracking Status - end --%>
            </div>
            <%-- Main - end --%>
        </div>
    </div>

    <script src="../resources/js/alert.js"></script>
    <script src="../resources/js/bootstrap.bundle.js"></script>
    <script src="../resources/js/custom.js"></script>
</body>
</html>
