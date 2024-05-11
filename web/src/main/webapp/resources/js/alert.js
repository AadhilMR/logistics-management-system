class Alert {
    /**
     * The alert.js file creates alert messages and render in the browser DOM
     * 
     */

    MESSAGE_ERROR = "error";
    MESSAGE_WARNING = "warning";
    MESSAGE_SUCCESS = "success";
    MESSAGE_INFO = "info";

    container = "";
    childContainer = "";

    constructor(messageType, message) {
        this.messageType = messageType;
        this.message = message;
    }

    createAlert() {
        // Message container
        var mainDiv = document.createElement("div");
        mainDiv.classList.add("message");
        mainDiv.id = "message";

        // Message content
        var contentSpan = document.createElement("span");
        contentSpan.classList.add("d-inline-flex");
        contentSpan.classList.add("align-items-center");

        // Message icon
        var icon = document.createElement("i");
        icon.classList.add("fa-solid");
        icon.classList.add("fs-3");
        
        switch (this.messageType) {
            case this.MESSAGE_ERROR:
                mainDiv.classList.add("message-error");
                icon.classList.add("fa-circle-exclamation");
                icon.classList.add("text-danger");
                break;
            case this.MESSAGE_WARNING:
                mainDiv.classList.add("message-warning");
                icon.classList.add("fa-circle-exclamation");
                icon.classList.add("text-warning");
                break;
            case this.MESSAGE_INFO:
                mainDiv.classList.add("message-info");
                icon.classList.add("fa-circle-info");
                icon.classList.add("text-primary");
                break;
            case this.MESSAGE_SUCCESS:
                mainDiv.classList.add("message-success");
                icon.classList.add("fa-circle-check");
                icon.classList.add("text-success");
                break;
        
            default:
                break;
        }

        // Message
        var messageSpan = document.createElement("span");
        messageSpan.style.paddingLeft = "7px";
        messageSpan.style.paddingRight = "7px";
        messageSpan.innerHTML = this.message;

        // Close button
        var closeBtn = document.createElement("i");
        closeBtn.classList.add("fa-solid");
        closeBtn.classList.add("fa-close");
        closeBtn.classList.add("cursor-hand");
        closeBtn.classList.add("alert-close-btn")
        closeBtn.style.position = "absolute";
        closeBtn.style.top = "0.3rem";
        closeBtn.style.right = "0.3rem";
        closeBtn.setAttribute("onclick", "hideAlert();");

        // Add childs into parent nodes
        contentSpan.appendChild(icon);
        contentSpan.appendChild(messageSpan);
        contentSpan.appendChild(closeBtn);

        mainDiv.appendChild(contentSpan);

        this.mainDiv = mainDiv;

        return this.mainDiv;
    }

    showMessage() {
        document.getElementsByClassName(this.container)[0].getElementsByClassName(this.childContainer)[0].appendChild(this.createAlert());

        setTimeout(() => {
            this.mainDiv.style.opacity = "1";
        }, 100);
    }

    hideMessage() {
        if(this.mainDiv) {
            var mainDiv = this.mainDiv;
            mainDiv.style.opacity = "0";

            setTimeout(() => {
                document.getElementsByClassName(this.container)[0].getElementsByClassName(this.childContainer)[0].removeChild(mainDiv);
            }, 900);
        }
    }

    show(container = "container-fluid", childContainer = "row") {

        this.container = container;
        this.childContainer = childContainer;

        this.showMessage();

        setTimeout(() => {
            this.hideMessage();
        }, 5000);
    }

    hide() {
        this.hideMessage();
    }
}