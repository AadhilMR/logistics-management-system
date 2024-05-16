<%--
  Created by IntelliJ IDEA.
  User: user
  Date: 5/11/2024
  Time: 6:37 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="../resources/fontawesome-free-6.5.2-web/css/all.css">
    <link rel="stylesheet" href="../resources/css/bootstrap.css">
    <link rel="stylesheet" href="../resources/css/custom.css">
</head>
<body onload="fetchAllTransactions();">
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
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item selected">
                                <i class="fa-solid fa-house-laptop"></i>
                                Dashboard
                            </div>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageCargo.jsp';">
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
                <h3>Current Transport Status</h3>

                <%-- Cargo status table - start --%>
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th scope="col">#</th>
                            <th scope="col">Origin</th>
                            <th scope="col">Destination</th>
                            <th scope="col">Last Known Location</th>
                            <th scope="col">Arrival On</th>
                            <th scope="col">Status</th>
                        </tr>
                    </thead>
                    <tbody class="table-group-divider" id="table_body">
                        <%-- Cargo Transaction will be loaded here --%>
                    </tbody>
                </table>
                <%-- Cargo status table - end --%>

            </div>
            <%-- Main - end --%>

            <%-- Cargo Details Modal - start --%>
            <div class="modal fade" id="cargoModal" tabindex="-1" aria-labelledby="cargoModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h1 class="modal-title fs-5" id="cargoModalLabel">Cargo Data</h1>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body">
                            <span class="my-1">
                                <span class="fw-bold">Tracking ID: </span>
                                <span>DHA912K</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Cargo ID: </span>
                                <span>C100T</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Cargo Descrition: </span>
                                <span>This is cargo description</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Origin Location: </span>
                                <span>CHINA (CHHKG)</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Destination Location: </span>
                                <span>SRI LANKA (SLCOL)</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Last Known Location: </span>
                                <span>INDIA (INDCH)</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Status: </span>
                                <span>IN TRANSIT</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Departure Date: </span>
                                <span>2024-05-09 12:33 GMT</span>
                            </span>
                            <br>
                            <span class="my-1">
                                <span class="fw-bold">Estimated Arrival Date: </span>
                                <span>2024-05-18 04:33 GMT</span>
                            </span>
                            <hr style="width: 90%; margin-left: 5%;">
                            <div class="col-12 mt-2">
                                <div class="row">
                                    <div class="col-12 mb-1"><label for="tr_status">Update Status</label></div>
                                    <div class="col-5 d-grid">
                                        <select id="tr_status" class="form-select form-border-primary">
                                            <option value="0">Select</option>
                                            <option value="1">In Transit</option>
                                            <option value="2">In Port</option>
                                            <option value="3">Unknown</option>
                                            <option value="4">Claimed</option>
                                            <option value="5">Not Received</option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
                            <button type="button" class="btn btn-primary">Save changes</button>
                        </div>
                    </div>
                </div>
            </div>
            <%-- Cargo Details Modal - end --%>
        </div>
    </div>

    <script src="../resources/js/alert.js"></script>
    <script src="../resources/js/bootstrap.bundle.js"></script>
    <script src="../resources/js/custom.js"></script>
</body>
</html>
