function applySettings(event) {

    event.preventDefault();

    const form = document.getElementById("applySettingsForm");
    const formData = new FormData(form);

    fetch("/users/updateuser", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {

        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            Cookies.set("UserID", responseData.UserID);
            Cookies.set("UserName", responseData.UserName);
            Cookies.set("Password", responseData.Password);
            Cookies.set("ConsumerKey", responseData.ConsumerKey);
            Cookies.set("ConsumerSecret", responseData.ConsumerSecret);
            Cookies.set("AccessKey", responseData.AccessKey);
            Cookies.set("AccessSecret", responseData.AccessSecret);
        }
    });
}