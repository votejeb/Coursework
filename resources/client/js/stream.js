function pageLoad(){
    document.getElementById("runStream").addEventListener("click", runStream);
}

function startStream(formData3){
    fetch("/twitter4j/streamdata", {method: 'post', body: formData3}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function runStream(event) {
    event.preventDefault();
    const form = document.getElementById("streamForm");
    const formData1 = new FormData(form);
    const formData2 = new FormData(form);
    formData1.delete("Language");

    fetch("/datasets/newset", {method: 'post', body: formData1}
    ).then(response => response.json()
    ).then(responseData1 => {
        if (responseData1.hasOwnProperty('error')) {
            alert(responseData1.error);
        }
        console.log(responseData1);

        fetch("/users/listone/"+Cookies.get("userid"), {method: 'get'}
        ).then(response => response.json()
        ).then(responseData => {
            console.log(responseData);
            var streamForm =new FormData();

            Object.keys(responseData).forEach(key => streamForm.append(key, responseData[key]));

            for (var pair of formData2.entries()) {
                streamForm.append(pair[0], pair[1]);
            }

            let e = parseInt(responseData1.SetID, 10);

            streamForm.append("PublicPrivate", responseData1.PublicPrivate);
            streamForm.append("TableID", e);

            var object = {};
            streamForm.forEach((value, key) => {object[key] = value});
            console.log(object);

            startStream(streamForm);
        });
    });

}