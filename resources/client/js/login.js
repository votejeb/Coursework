function pageLoad() {
    //checks login status
    if(window.location.search === '?logout') {
        //logs user out
        document.getElementById('content').innerHTML = '<h1>Logging out, please wait...</h1>';
        logout();
    } else {
        //logs user in and gives login form when event is called
        document.getElementById("loginButton").addEventListener("click", login);
    }

}

function login(event) {

    event.preventDefault();
    //fetches form data
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    //calls server function to login
    fetch("/users/login", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            //sets site cookies
            Cookies.set("UserName", responseData.username);
            Cookies.set("token", responseData.token);
            Cookies.set("userid", responseData.userid);
            //redirects page
            window.location.href = '/client/index.html';
        }
    });
}
//logout function
function logout() {
    //removes cookies and token from database
    fetch("/users/logout", {method: 'post'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {

            alert(responseData.error);

        } else {

            Cookies.remove("UserName");
            Cookies.remove("token");
            Cookies.remove("userid");

            window.location.href = '/client/index.html';

        }
    });

}
