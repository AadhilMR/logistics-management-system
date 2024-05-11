<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 5/11/2024
  Time: 10:01 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Manage Cargo</title>
    <link rel="stylesheet" href="../resources/fontawesome-free-6.5.2-web/css/all.css">
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
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item selected">
                                <i class="fa-solid fa-cart-flatbed"></i>
                                Cargo
                            </div>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageRoute.jsp';">
                                <i class="fa-solid fa-route"></i>
                                Book & Edit Route
                            </div>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='trackCargo.jsp';">
                                <i class="fa-solid fa-magnifying-glass-location"></i>
                                Track Cargo
                            </div>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageUsers.jsp';">
                                <i class="fa-solid fa-user-group"></i>
                                Users
                            </div>
                            <hr style="width: 90%; margin-left: 5%;">
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item nav-logout" onclick="window.location='../index.jsp';">
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

                <%-- Cargo Creation Container - start --%>
                <h3>Create Cargo</h3>
                <div class="row px-3 mb-5">
                    <div class="col-12 rounded-3 container-shadow px-2 py-3">
                        <div class="row">
                            <div class="col-6">
                                <label for="cargo_desc" class="fw-bold">Description</label><br>
                                <span style="font-size: 0.9rem;" class="fw-light">Write short description (Not necessary)</span>
                                <textarea id="cargo_desc" class="form-control col-6" cols="30" rows="4" style="resize: none;"></textarea>
                            </div>
                            <div class="col-6">
                                <label for="cargo_ins" class="fw-bold">Special Instructions</label><br>
                                <span style="font-size: 0.9rem;" class="fw-light">Seperate instructions with a comma (Not necessary)</span>
                                <textarea id="cargo_ins" class="form-control col-6" cols="30" rows="4" style="resize: none;"></textarea>
                            </div>
                            <div class="col-12 mt-3 text-end">
                                <button class="btn btn-danger col-auto align-self-end">Create Cargo</button>
                            </div>
                        </div>
                    </div>
                </div>
                <%-- Cargo Creation Container - end --%>

                <%-- Cargo status table - start --%>
                <h3>Cargo List</h3>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Description</th>
                            <th scope="col">Created Date</th>
                            <th scope="col">Special Instructions</th>
                            <th scope="col">Status</th>
                            <th scope="col">Tracking ID</th>
                        </tr>
                    </thead>
                    <tbody class="table-group-divider">
                        <tr>
                            <th scope="row">C100T</th>
                            <td>
                                This is the description of cargo
                            </td>
                            <td>2024-05-11 10:10:34</td>
                            <td>
                                Fragile Item<br>
                                This side up
                            </td>
                            <td><span class="badge cargo-status-not-specified">Select Route</span></td>
                            <td>N/A</td>
                        </tr>
                        <tr>
                            <th scope="row">C200T</th>
                            <td>
                                This is the description of cargo
                            </td>
                            <td>2024-05-11 10:16:59</td>
                            <td>
                                Flammable
                            </td>
                            <td><span class="badge cargo-status-route-specified">Route Selected</span></td>
                            <td>DHS012K</td>
                        </tr>
                    </tbody>
                </table>
                <%-- Cargo status table - end --%>

            </div>
            <%-- Main - end --%>
        </div>
    </div>

    <script src="../resources/js/alert.js"></script>
    <script src="../resources/js/bootstrap.bundle.js"></script>
    <script src="../resources/js/custom.js"></script>
</body>
</html>
