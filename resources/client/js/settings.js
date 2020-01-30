function pageLoad(){
    //adds event listener to to applyChanges button
    document.getElementById("applyChanges").addEventListener("click", applySettings);
    document.getElementById("deleteUser").addEventListener("click", deleteUser);
}

function applySettings(event) {

    event.preventDefault();
    //fetches user data
    fetch("/users/listone/"+Cookies.get("userid"), {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
        //console logs to check correct data has been retrieved
        console.log(responseData);

        const formData = new FormData();
        //converts json response to formdata type
        Object.keys(responseData).forEach(key => formData.append(key, responseData[key]));
        //fetches formdata from settings page
        const form = document.getElementById("applySettingsForm");
        const newFormData = new FormData(form);
        //deletes null entries in form submit
        for (var pair2 of newFormData.entries()) {
            if (pair2[1]==null){
                newFormData.delete(pair2[0])
            }
        }
        //sets new formdata parameters
        for (var pair1 of newFormData.entries()) {
            formData.set(pair1[0], pair1[1]);
        }
        //updates user settings with new parameters
        fetch("/users/updateuser", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            }
        });

    });
}

function deleteUser(event){
    event.preventDefault();
    let formData = new FormData;
    formData.append("UserID",parseInt(Cookies.get("userid"),10));
    fetch("/users/deleteuser", {method: 'post', body: formData}
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

    })
}
