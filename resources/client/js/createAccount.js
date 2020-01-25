function pageLoad(){
    document.getElementById("createButton").addEventListener("click", createAccount);
}

function createAccount(){
    const form = document.getElementById("createForm");
    const formData = new FormData(form);

    fetch("/users/newuser", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}