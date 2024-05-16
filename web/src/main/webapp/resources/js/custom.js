let alertMessage;

function hideAlert() {
    alertMessage.hide();
}

/**
 * dashboard.jsp
 */

function openCargoModal(trackingId) {
    const cargoModal = new bootstrap.Modal(document.getElementById("cargoModal"));
    cargoModal.show();
}

function fetchAllTransactions() {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonArr = JSON.parse(request.responseText);

            document.getElementById("table_body").innerText = "";

            for(var i=0; i<jsonArr.length; i++) {
                var jsonObj = jsonArr[i];
                loadTransactionsToTable(jsonObj);
            }
        }
    };

    request.open("GET", "../transactions", true);
    request.send();
}

function loadTransactionsToTable(jsonObj) {
    var tableBody = document.getElementById("table_body");

    var trackingId = jsonObj.trackingId;
    var cargoId = jsonObj.cargoId;
    var desc = jsonObj.cargoDescription;
    var originName = jsonObj.originName;
    var originCode = jsonObj.originCode;
    var destName = jsonObj.destinationName;
    var destCode = jsonObj.destinationCode;
    var lastLocName = jsonObj.lastLocationName;
    var lastLocCode = jsonObj.lastLocationCode;
    var status = jsonObj.transportStatus;
    var deptTime = jsonObj.departureDateTime;
    var arrTime = jsonObj.arrivalDateTime;

    var row = document.createElement("tr");

    var th = document.createElement("th");
    th.scope = "row";
    th.classList.add("cursor-hand");
    th.setAttribute("onclick", "openCargoModal('"+ trackingId +"');");
    th.innerText = trackingId;
    row.appendChild(th);

    var tdOrigin = document.createElement("td");
    var brOrigin = document.createElement("br");
    var spanOrigin = document.createElement("span");
    spanOrigin.classList.add("tr-location");
    spanOrigin.innerText = originCode;
    tdOrigin.append(originName);
    tdOrigin.append(brOrigin);
    tdOrigin.append(spanOrigin);
    row.appendChild(tdOrigin);

    var tdDest = document.createElement("td");
    var brDest = document.createElement("br");
    var spanDest = document.createElement("span");
    spanDest.classList.add("tr-location");
    spanDest.innerText = destCode;
    tdDest.append(destName);
    tdDest.append(brDest);
    tdDest.append(spanDest);
    row.appendChild(tdDest);

    var tdLast = document.createElement("td");
    var brLast = document.createElement("br");
    var spanLast = document.createElement("span");
    spanLast.classList.add("tr-location");
    spanLast.innerText = lastLocCode;
    tdLast.append(lastLocName);
    tdLast.append(brLast);
    tdLast.append(spanLast);
    row.appendChild(tdLast);

    var tdDate = document.createElement("td");
    tdDate.innerText = arrTime;
    row.appendChild(tdDate);

    var tdStatus = document.createElement("td");
    var spanStatus = document.createElement("span");
    spanStatus.classList.add("badge");

    switch (status) {
        case "IN_TRANSIT":
            spanStatus.classList.add("tr-status-transit");
            break;
        case "IN_PORT":
            spanStatus.classList.add("tr-status-port");
            break;
        case "UNKNOWN":
            spanStatus.classList.add("tr-status-unknown");
            break;
        case "CLAIMED":
            spanStatus.classList.add("tr-status-claimed");
            break;
        case "NOT_RECEIVED":
            spanStatus.classList.add("tr-status-lost");
            break;
    }

    spanStatus.innerText = status;
    tdStatus.appendChild(spanStatus);
    row.appendChild(tdStatus);

    tableBody.appendChild(row);
}

function updateTransportStatus() {
    var trackingId = document.getElementById("tracking_id");
    var newStatus = document.getElementById("transport_status");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            console.log(request.responseText);
        }
    };

    request.open("GET", "../updateStatus?id=" + trackingId.innerText + "&status=" + newStatus.value, true);
    request.send();
}

/**
 * manageUsers.jsp
 */

function registerUser() {
    var username = document.getElementById("username");
    var password = document.getElementById("password");
    var userTypeId = document.getElementById("usertype");

    var data = {
        username: username.value,
        password: password.value,
        usertype: userTypeId.value
    };

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            if(request.responseText === "success") {
                alertMessage = new Alert("success", "The user is registered successfully!");
                alertMessage.show();

                username.value = "";
                password.value = "";
                userTypeId.value = -1;
            } else {
                alertMessage = new Alert("error", "Something went wrong! Please try again later.");
                alertMessage.show();
            }
        }
    }

    request.open("POST", "../register", true);
    request.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    request.send(JSON.stringify(data));
}

/**
 * manageRoute.jsp
 */

function fetchTerminals() {
    var terminal_selector = document.getElementById("terminal_selector");
    var origin_selector = document.getElementById("origin");
    var destination_selector = document.getElementById("destination");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonArr = JSON.parse(request.responseText);

            for(var i=0; i<jsonArr.length; i++) {
                var jsonObj = jsonArr[i];

                var value = jsonObj.id;
                var name = jsonObj.name + " ("+ jsonObj.code +")";

                var option = document.createElement("option");
                option.value = value;
                option.innerText = name;
                terminal_selector.appendChild(option);

                option = document.createElement("option");
                option.value = value;
                option.innerText = name;
                origin_selector.appendChild(option);

                option = document.createElement("option");
                option.value = value;
                option.innerText = name;
                destination_selector.appendChild(option);
            }
        }
    };

    request.open("GET", "../terminals", true);
    request.send();
}

function addRoutes() {
    var terminal_selector = document.getElementById("terminal_selector");
    var route_terminals = document.getElementById("route_terminals");

    var selectedOption = terminal_selector.options[terminal_selector.selectedIndex].text.split("(")[0];
    var currentText = route_terminals.value;

    route_terminals.innerText = (currentText.length == 0) ? selectedOption : currentText + ", " + selectedOption;
}

function createRoute() {
    var route = document.getElementById("route_name");
    var terminals = document.getElementById("route_terminals");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonObj = JSON.parse(request.responseText);

            if(jsonObj.response === "success") {
                alertMessage = new Alert("success", "New route created successfully!");
                alertMessage.show();

                fetchRoutes();
            } else {
                alertMessage = new Alert("error", "Something went wrong! Please try again later.");
                alertMessage.show();
            }
        }
    }

    request.open("GET", "../createRoute?route=" + route.value + "&terminals=" + terminals.value, true);
    request.send();
}

function fetchRoutes() {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonArr = JSON.parse(request.responseText);

            document.getElementById("table_body").innerText = "";

            for(var i=0; i<jsonArr.length; i++) {
                var jsonObj = jsonArr[i];

                var routeId = jsonObj.id;
                var routeNmae = jsonObj.name;
                var nextDepartureTime = jsonObj.nextDepartureTime;
                var days = jsonObj.daysForVoyage;
                var terminals = jsonObj.terminalList;

                // Add routes to Routes Table
                loadRoutes(routeNmae, days, terminals , nextDepartureTime);

                // Add routes to Route Selector
                var option = document.createElement("option");
                option.value = routeId;
                option.innerText = routeNmae;
                document.getElementById("route_selector").appendChild(option);
            }

            loadNextRouteName(jsonArr.length);
        }
    };

    request.open("GET", "../routes", true);
    request.send();
}

function loadRoutes(name, days, terminals, departureTime) {
    var tbody = document.getElementById("table_body");

    var row = document.createElement("tr");
    var cell = document.createElement("td");

    var spanTitle = document.createElement("span");
    spanTitle.classList.add("fw-bold");
    spanTitle.innerText = name + " - " + days + " Days";

    var spanDepartureTime = document.createElement("span");
    spanDepartureTime.classList.add("fw-bold");
    spanDepartureTime.innerText = "Next Departure Time: " + departureTime;

    var div = document.createElement("div");
    div.classList.add("offset-1");

    for(var i=0; i<terminals.length-1; i++) {
        var spanTerm = document.createElement("span");
        var icon = document.createElement("i");
        icon.classList.add("fa-solid", "fa-right-long", "text-color-primary");
        spanTerm.append(terminals[i] + " ");
        spanTerm.appendChild(icon);
        spanTerm.append(" " + terminals[i+1]);

        div.appendChild(spanTerm);

        if(i !== (terminals.length-2)) {
            div.appendChild(document.createElement("br"));
        }
    }

    cell.appendChild(spanTitle);
    cell.appendChild(div);
    cell.appendChild(spanDepartureTime);

    row.appendChild(cell);
    tbody.appendChild(row);
}

function loadNextRouteName(currentRouteCount) {
    var nextRouteName = "Route " + (currentRouteCount + 1);
    document.getElementById("route_name").value = nextRouteName;
}

function fetchCargoIds() {
    var select = document.getElementById("cargo");
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonArr = JSON.parse(request.responseText);

            for(var i=0; i<jsonArr.length; i++) {
                var jsonObj = jsonArr[i];
                var id = jsonObj.cargoId;

                var option = document.createElement("option");
                option.value = id;
                option.innerText = id;
                select.appendChild(option);
            }
        }
    };

    request.open("GET", "../cargos", true);
    request.send();
}

function setRoute() {
    var cargo = document.getElementById("cargo");
    var origin = document.getElementById("origin");
    var destination = document.getElementById("destination");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonObj = JSON.parse(request.responseText);

            if(jsonObj.response === "success") {
                alertMessage = new Alert("success", "A route is set to the cargo.");
                alertMessage.show();

                document.getElementById("selected_route_name").innerText = jsonObj.route;
                document.getElementById("tracking_id").innerText = jsonObj.trackingId;

                var container = document.getElementById("route_track_container");
                container.classList.remove("d-none");
            } else {
                alertMessage = new Alert("error", "Something went wrong! Please try again later.");
                alertMessage.show();
            }
        }
    };

    var url = "../createTransaction?cargo=" + cargo.value + "&origin=" + origin.value + "&dest=" + destination.value;
    request.open("GET", url, true);
    request.send();
}

/**
 * manageCargo.jsp
 */

function createCargo() {
    var desc = document.getElementById("cargo_desc");
    var ins = document.getElementById("cargo_ins");

    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            if(request.responseText === "success") {
                alertMessage = new Alert("success", "Cargo created successfully!");
                alertMessage.show();

                fetchCargos();
            } else {
                alertMessage = new Alert("error", "Something went wrong! Plese try again later.");
                alertMessage.show();
            }
        }
    };

    request.open("GET", "../createCargo?desc=" + desc.value + "&ins=" + encodeURIComponent(ins.value), true);
    request.setRequestHeader("content-type", "application/x-www-form-urlencoded");
    request.send();
}

function fetchCargos() {
    var request = new XMLHttpRequest();

    request.onreadystatechange = function () {
        if(request.readyState === 4) {
            var jsonArr = JSON.parse(request.responseText);

            document.getElementById("table_tbody").innerText = "";

            for(var i=0; i<jsonArr.length; i++) {
                var jsonObj = jsonArr[i];

                var id = jsonObj.cargoId;
                var desc = jsonObj.cargoDescription;
                var ins = jsonObj.cargoInstruction;
                var createdDate = jsonObj.createdDate;
                var status = jsonObj.cargoStatus;

                loadCargos(id, desc, createdDate, ins, status);
            }
        }
    };

    request.open("GET", "../cargos", true);
    request.send();
}

function loadCargos(id, description, createdDate, instruction, status) {
    var tbody = document.getElementById("table_tbody");

    var row = document.createElement("tr");

    var cellId = document.createElement("th");
    cellId.scope = "row";
    cellId.innerText = id;

    var cellDesc = document.createElement("td");
    cellDesc.innerText = (description != null || description != "") ? description : "N/A";

    var cellDate = document.createElement("td");
    cellDate.innerText = createdDate;

    var cellIns = document.createElement("td");
    cellIns.innerText = (instruction != null || instruction != "") ? instruction : "N/A";

    var cellStatus = document.createElement("td");
    var spanStatus = document.createElement("span");

    if(status === "not-specified") {
        spanStatus.classList.add("badge", "cargo-status-not-specified");
        spanStatus.innerText = "Select Route";
    } else {
        spanStatus.classList.add("badge", "cargo-status-route-specified");
        spanStatus.innerText = "Route Selected";
    }
    cellStatus.appendChild(spanStatus);

    row.appendChild(cellId);
    row.appendChild(cellDesc);
    row.appendChild(cellDate);
    row.appendChild(cellIns);
    row.appendChild(cellStatus);
    tbody.appendChild(row);
}