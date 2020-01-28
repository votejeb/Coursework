function pageLoad(){
    var formData=new FormData;
    formData.append(,);
    fetch("/users/createuser/", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        console.log(responseData);
    });
}