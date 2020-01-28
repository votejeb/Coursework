function pageLoad(){
    viewFilterTable();
    document.getElementById("createFilter").addEventListener("click", createFilter);
    document.getElementById("updateFilter").addEventListener("click", updateFilter);
    document.getElementById("deleteFilter").addEventListener("click", deleteFilter);
    document.getElementById("addFilterWord").addEventListener("click", add1Filter);
    document.getElementById("viewFilter").addEventListener("click", read1Filter);
    document.getElementById("deleteFilterWord").addEventListener("click", delete1Filter);
}

function viewFilterTable(){
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>Data Filter ID</th>' +
        '<th>|Data Filter Name</th>' +
        '<th>|Whitelist Status</th>' +
        '<th>|Public Status</th>' +
        '</tr>';

    let UserID=Cookies.get("userid");
    fetch("/datafilters/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
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
        document.getElementById("filterHTML").innerHTML = filterHTML;
    })
}

function createFilter(){
    const form = document.getElementById("createForm");
    const formData = new FormData(form);
    let UserID=Cookies.get("userid");
    formData.append("UserID", UserID);
    fetch("/datafilters/newfilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function updateFilter(){
    const form = document.getElementById("updateForm");
    const formData = new FormData(form);
    fetch("/datafilters/updatefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function deleteFilter(){
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

function add1Filter(){
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

function read1Filter(){
    debugger;
    const form = document.getElementById("viewFilterForm");
    const formData = new FormData(form);
    let FilterID = formData.get("DataFilterID");
    console.log(FilterID);
    let viewFilterHTML = '<table>' +
        '<tr>' +
        '<th>Filter Contents</th>' +
        '</tr>';
    fetch("/linkedfilters/readfilter/"+FilterID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (var i=0; i<responseData.length; i++){
                viewFilterHTML += `<tr>` +
                    `<td>${responseData[i].Words}</td>` +
                    `</tr>`;
            }
            viewFilterHTML += '</table>'
        }
        document.getElementById("viewFilterHTML").innerHTML = viewFilterHTML;
    })
}

function delete1Filter(){
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
