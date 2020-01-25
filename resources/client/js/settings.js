function pageLoad(){
    document.getElementById("applyChanges").addEventListener("click", applySettings);
}

function applySettings(event) {

    event.preventDefault();

    fetch("/users/listone/"+Cookies.get("userid"), {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
        console.log(responseData);

        const formData = new FormData();

        Object.keys(responseData).forEach(key => formData.append(key, responseData[key]));

    const form = document.getElementById("applySettingsForm");
    const newFormData = new FormData(form);

        for (var pair2 of newFormData.entries()) {
            if (pair2[1]==null){
                newFormData.delete(pair2[0])
            }
        }
        for (var pair1 of newFormData.entries()) {
            formData.set(pair1[0], pair1[1]);
        }

        fetch("/users/updateuser", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            }
        });

    });


}