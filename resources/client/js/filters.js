function pageLoad(){
    viewFilterTable(Cookies.get("userid"));
    document.getElementById("createFilter").addEventListener("click", createFilter);
    document.getElementById("updateFilter").addEventListener("click", updateFilter);
    document.getElementById("deleteFilter").addEventListener("click", deleteFilter);
    document.getElementById("addFilterWord").addEventListener("click", add1Filter);
    document.getElementById("viewFilter").addEventListener("click", read1Filter);
    document.getElementById("deleteFilterWord").addEventListener("click", delete1Filter);
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
                    `<td>${responseData[i].DataFilterID|}</td>` +
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
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/datafilters/newfilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function updateFilter(){
    const form = document.getElementById("loginForm");
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
    const form = document.getElementById("loginForm");
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
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/linkedfilter/readfilter/"+filterID, {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}

function read1Filter(){
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/datafilters/listone/"+FilterID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {

    })
}

function delete1Filter(){
    const form = document.getElementById("loginForm");
    const formData = new FormData(form);
    fetch("/linkedfilters/deletefilter", {method: 'post', body: formData}
    ).then(response => response.json()
    ).then(responseData => {
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        }
    });
}
