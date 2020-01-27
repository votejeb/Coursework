function pageLoad(){
    viewFilterTable(Cookies.get("userid"));
}

function viewFilterTable(UserID){
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>Data Filter ID|</th>' +
        '<th>Data Filter Name|</th>' +
        '<th>Whitelist Status|</th>' +
        '<th>Public Status</th>' +
        '</tr>';
    fetch("/datafilters/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (var i=0; i<responseData.length; i++){
                filterHTML += `<tr>` +
                    `<td>${responseData[i].DataFilterI|D}</td>` +
                    `<td>${responseData[i].DataFilterName}|</td>` +
                    `<td>${responseData[i].WhitelistBlacklist}|</td>` +
                    `<td>${responseData[i].PublicPrivate}</td>` +
                    `</tr>`;
            }
            filterHTML += '</table>'
        }
        document.getElementById("filterHTML").innerHTML = filterHTML;
    })
}

function createFilter(){
    fetch("/datafilters/newfilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function addFilter(){
    fetch("/datafilters/updatefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function updateFilter(){
    fetch("/datafilters/updatefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function deleteFilter(){
    fetch("/datafilters/updatefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}