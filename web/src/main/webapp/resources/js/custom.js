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