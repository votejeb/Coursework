pageLoad(){
    var formData=new FormData;
    fetch("/users/createuser", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        console.log(responseData);
    });
}