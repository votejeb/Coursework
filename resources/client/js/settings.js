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
            Cookies.set("UserName", responseData.username);
            Cookies.set("Password", responseData.password);
            Cookies.set("ConsumerKey", responseData.consumerkey);
            Cookies.set("ConsumerSecret", responseData.consumersecret);
            Cookies.set("AccessKey", responseData.accesskey);
            Cookies.set("AccessSecret", responseData.accesssecret);
        }
    });
}