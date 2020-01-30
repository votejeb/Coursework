function pageLoad(){
    //loads all user associated filters
    viewFilterTable();
    //sets event listeners for js events
    document.getElementById("createFilter").addEventListener("click", createFilter);
    document.getElementById("updateFilter").addEventListener("click", updateFilter);
    document.getElementById("deleteFilter").addEventListener("click", deleteFilter);
    document.getElementById("addFilterWord").addEventListener("click", add1Filter);
    document.getElementById("viewFilter").addEventListener("click", read1Filter);
    document.getElementById("deleteFilterWord").addEventListener("click", delete1Filter);
    document.getElementById("deleteFilter").addEventListener("click", deleteFilter);
}
//filter viewer
function viewFilterTable(){
    //sets table headers
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>Data Filter ID</th>' +
        '<th>|Data Filter Name</th>' +
        '<th>|Whitelist Status</th>' +
        '<th>|Public Status</th>' +
        '</tr>';

    let UserID=Cookies.get("userid");
    //fetches all relevant data from server
    fetch("/datafilters/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            //html constructor
            for (var i=0; i<responseData.length; i++){
                filterHTML += `<tr>` +
                    `<td>${responseData[i].DataFilterID}</td>` +
                    `<td>|${responseData[i].DataFilterName}</td>` +
                    `<td>|${responseData[i].WhitelistBlacklist}</td>` +
                    `<td>|${responseData[i].PublicPrivate}</td>` +
                    `</tr>`;
            }
            filterHTML += '</table>'
        }
        //assigns html element to variable
        document.getElementById("filterHTML").innerHTML = filterHTML;
    })
}
//filter creator
function createFilter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("createForm");
    const formData = new FormData(form);
    let UserID=Cookies.get("userid");
    //adds userid to submitted formdata, required for java call
    formData.append("UserID", UserID);
    fetch("/datafilters/newfilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
//filter updater
function updateFilter(event){
    event.preventDefault();
    //debugger for debugging, will be removed in final version
    debugger;
    //formdata constructor
    const form = document.getElementById("updateForm");
    const newFormData = new FormData(form);
    let UserID=Cookies.get("userid");
    let FilterID=newFormData.get("DataFilterID");
    fetch("datafilters/listone/"+UserID, {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
        console.log(responseData);
        responseData=responseData.DataFilterID===FilterID;
        const formData = new FormData();
        //converts json response to formdata type
        Object.keys(responseData).forEach(key => formData.append(key, responseData[key]));

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
        //updates filter
        fetch("/datafilters/updatefilter", {method: 'post', body: formData}
        ).then(response => response.json()
        ).then(responseData => {
            if (responseData.hasOwnProperty('error')) {
                alert(responseData.error);
            }
        });
    });
}
//delets set of filters
function deleteFilter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("deleteForm");
    const formData = new FormData(form);
    fetch("/datafilters/deletefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
//single filter adder
function add1Filter(event){
    event.preventDefault();
    const form = document.getElementById("modifyFilterForm");
    const formData = new FormData(form);
    fetch("/linkedfilters/newfilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
//read table of filter words
function read1Filter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("viewFilterForm");
    const formData = new FormData(form);
    //table constructor
    let viewFilterHTML = '<table>' +
        '<tr>' +
        '<th>Filter Contents</th>' +
        '</tr>';

    fetch("/linkedfilters/readfilter/"+formData.get("DataFilterID"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        //responsedata log to check correct server response
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            //table constructor
            for (var i=0; i<responseData.length; i++){
                viewFilterHTML += `<tr>` +
                    `<td>${responseData[i].Words}</td>` +
                    `</tr>`;
            }
            viewFilterHTML += '</table>'
        }
        //html assigning
        document.getElementById("viewFilterHTML").innerHTML = viewFilterHTML;
    })
}
//delete single filter word
function delete1Filter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("modifyFilterForm");
    const formData = new FormData(form);
    fetch("/linkedfilters/deletefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

//delete whole filter filter
function deleteFilter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("deleteFilterForm");
    const formData = new FormData(form);
    fetch("/datafilters/deletefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
