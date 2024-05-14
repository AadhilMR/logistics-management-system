let alertMessage;

function hideAlert() {
    alertMessage.hide();
}

function openCargoModal(trackingId) {
    const cargoModal = new bootstrap.Modal(document.getElementById("cargoModal"));
    cargoModal.show();
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