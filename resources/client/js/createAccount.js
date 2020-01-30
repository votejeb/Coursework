function pageLoad(){
    //set event listener to trigger account creation
    document.getElementById("createButton").addEventListener("click", createAccount);
}

function createAccount(event){
    debugger;
    event.preventDefault();
    //formdata import
    const form = document.getElementById("createForm");
    const formData = new FormData(form);
    //data retrieval
    fetch("/users/newuser", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            window.location.href = '/client/index.html';
        }
    });
}