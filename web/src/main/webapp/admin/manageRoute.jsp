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
    <title>Track Cargo</title>
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
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item" onclick="window.location='manageCargo.jsp';">
                                <i class="fa-solid fa-cart-flatbed"></i>
                                Cargo
                            </div>
                            <div class="col-10 offset-1 px-2 py-2 mb-3 nav-item selected">
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
                <div class="row">
                    <div class="col-8">
                        <%-- Cargo Booking Container - start --%>
                        <h3>Book Cargo</h3>
                        <div class="row px-3 mb-5">
                            <div class="col-12 rounded-3 container-shadow px-2 py-3">
                                <div class="row">
                                    <div class="col-6">
                                        <label for="cargo" class="fw-bold">Select Cargo</label><br>
                                        <select id="cargo" class="form-select">
                                            <option value="0">Select</option>
                                            <option value="1">DHS012K</option>
                                            <option value="2">DHS012K</option>
                                            <option value="3">DHS012K</option>
                                        </select>
                                    </div>
                                    <div class="col-6">
                                        <label for="route" class="fw-bold">Select Route</label><br>
                                        <select id="route" class="form-select">
                                            <option value="0">Select</option>
                                            <option value="1">Route 01</option>
                                            <option value="2">Route 02</option>
                                        </select>
                                    </div>
                                    <div class="col-12 mt-3 text-end">
                                        <button class="btn btn-danger col-auto align-self-end">Set Route</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%-- Cargo Booking Container - end --%>

                        <%-- Route Creation Container - start --%>
                        <h3>Create Route</h3>
                        <div class="row px-3 mb-5">
                            <div class="col-12 rounded-3 container-shadow px-2 py-3">
                                <div class="row">
                                    <div class="col-6">
                                        <label for="route_name" class="fw-bold">Route Name</label><br>
                                        <span style="font-size: 0.9rem;" class="fw-light">This is a auto-generated name</span>
                                        <input type="text" class="form-control" id="route_name" value="Route 3" readonly>
                                    </div>
                                    <div class="col-12 mt-2">
                                        <label for="terminal_selector" class="fw-bold">Terminals</label><br>
                                        <div class="row">
                                            <div class="col-6">
                                                <div class="row">
                                                    <div class="col-9">
                                                        <select id="terminal_selector" class="form-select">
                                                            <option value="0">Select</option>
                                                            <option value="1">China (CHHKG)</option>
                                                        </select>
                                                    </div>
                                                    <div class="col-3 d-grid">
                                                        <button class="btn btn-danger">Add</button>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-6">
                                                <textarea id="route_terminals" class="form-control col-6" cols="30" rows="4" style="resize: none;" readonly></textarea>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-12 mt-3" style="font-size: 0.9rem;">
                                        <span>* The first terminal you selected is the Origin of the route</span><br>
                                        <span>* The last terminal you selected is the Destination of the route</span><br>
                                        <span>* Route Name and the Voyage Duration will be calculated automatically</span><br>
                                    </div>
                                    <div class="col-12 mt-1 text-end">
                                        <button class="btn btn-danger col-auto align-self-end">Create Route</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%-- Route Creation Container - end --%>
                    </div>
                    <div class="col-4">
                        <%-- Route List Table - start --%>
                        <h3>Route List</h3>
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th scope="col">Route</th>
                                </tr>
                            </thead>
                            <tbody class="table-group-divider">
                                <tr>
                                    <td>
                                        <span class="fw-bold">Route 01 - 18 Days</span>
                                        <div class="offset-1">
                                            <span>China <i class="fa-solid fa-right-long text-color-primary"></i> Korea</span>
                                            <br>
                                            <span>Korea <i class="fa-solid fa-right-long text-color-primary"></i> India</span>
                                            <br>
                                            <span>India <i class="fa-solid fa-right-long text-color-primary"></i> Sri Lanka</span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="fw-bold">Route 02 - 72 Days</span>
                                        <div class="offset-1">
                                            <span>USA <i class="fa-solid fa-right-long bg-color-primary"></i> UK</span>
                                            <br>
                                            <span>UK <i class="fa-solid fa-right-long bg-color-primary"></i> South Africa</span>
                                            <br>
                                            <span>South Africa <i class="fa-solid fa-right-long bg-color-primary"></i> Madagaskar</span>
                                            <br>
                                            <span>Madagaskar <i class="fa-solid fa-right-long bg-color-primary"></i> India</span>
                                            <br>
                                            <span>India <i class="fa-solid fa-right-long bg-color-primary"></i> Sri Lanka</span>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                        <%-- Route list table - end --%>
                    </div>
                </div>
            </div>
            <%-- Main - end --%>
        </div>
    </div>

    <script src="../resources/js/bootstrap.bundle.js"></script>
    <script src="../resources/js/custom.js"></script>
</body>
</html>
