//index pageload function, is called on every page
function pageLoadI(){
    checkLogin();
    //this chunk of code sets up the page redirection for each page. This layout saves developmnet time as this HTML layout only has to be written out once
    document.getElementById("content").innerHTML = `<div class="genericDiv">` +
        `<input type="button" value="Filters" id="filtersButton" onclick="window.location.href = '/client/filters.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Run a Stream" id="streamButton" onclick="window.location.href = '/client/stream.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Process some data" id="processButton" onclick="window.location.href = '/client/process.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Settings" id="settingsButton" onclick="window.location.href = '/client/settings.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Admin Options" id="adminButton" onclick="window.location.href = '/client/admin.html'">` +
        `</div>`;
}
//this function checks the login status and displays the relevant redirect at the bottom of the page
function checkLogin() {

    let username = Cookies.get("UserName");
    let logInHTML = '';
    if (username === undefined) {
        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";
    } else {
        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";

    }
    document.getElementById("loggedInDetails").innerHTML = logInHTML;
}
