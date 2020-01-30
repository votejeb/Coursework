function pageLoad(){
    //sets event listener
    document.getElementById("runStream").addEventListener("click", runStream);
}

function startStream(formData3){
    //startstream function, is called when all formdata parameters are gathered
    fetch("/twitter4j/streamdata", {method: 'post', body: formData3}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
//gathers correct formdata parameters to be submitted to startStream
function runStream(event) {
    event.preventDefault();
    const form = document.getElementById("streamForm");
    const formData1 = new FormData(form);
    const formData2 = new FormData(form);
    formData1.delete("Language");
    //creates new data set
    fetch("/datasets/newset", {method: 'post', body: formData1}
    ).then(response => response.json()
    ).then(responseData1 => {
        if (responseData1.hasOwnProperty('error')) {
            alert(responseData1.error);
        }
        //console log for bugfixing and checking all data points are laoded
        console.log(responseData1);
        //fetches userdata parameters
        fetch("/users/listone/"+Cookies.get("userid"), {method: 'get'}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData1.hasOwnProperty('error')) {
                alert(responseData1.error);
            }
            console.log(responseData);
            var streamForm =new FormData();
            //converts response from server to a formdata type
            Object.keys(responseData).forEach(key => streamForm.append(key, responseData[key]));
            //adds gathered server parameters to stream form
            for (var pair of formData2.entries()) {
                streamForm.append(pair[0], pair[1]);
            }
            //converts setid to int datatype
            let e = parseInt(responseData1.SetID, 10);
            //appends more parameters to streamform
            streamForm.append("PublicPrivate", responseData1.PublicPrivate);
            streamForm.append("TableID", e);
            //streamform checker to display all key value pairs to console
            var object = {};
            streamForm.forEach((value, key) => {object[key] = value});
            console.log(object);
            //stream activation
            startStream(streamForm);
        });
    });
}