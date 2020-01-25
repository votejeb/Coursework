var config={
    type: 'line',
    data: {
        labels: [],
        datasets: [{
            label: [],
            data: [],
            backgroundColor: [],
            borderColor: [],
            fill:false
        }]
    },
    options: {
        legend: {
            display: false
        },
        scales: {
            yAxes: [{
                ticks: {
                    beginAtZero: true
                }
            }]
        },
        responsive: false
    }
}

window.onload = function() {
    pageLoad();
    viewTable(Cookies.get("userid"));
    var tableID="2";
    var RawSets="2020_01_19_20_34_14-2020_01_19_20_39_15";
    var ctx = document.getElementById('chartCanvas').getContext('2d');
    window.myLine = new Chart(ctx, config);
    fetch("/processeddatas/readkeywords/"+tableID+"/"+RawSets, {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {

        var RawSets1=RawSets.split("-");
        for (var index3 = 0; index3 < RawSets1.length; index3++) {
            config.data.labels.push(RawSets1[index3]);
        }

        for (var index2 = 0; index2 < responseData.length; index2++) {
            var datas=responseData[index2];
            var newDataset = {
                label: [],
                backgroundColor: getRandomColour(),
                borderColor: getRandomColour(),
                data: [],
                fill: false
            };
            newDataset.label.push(datas.Words);
            var jeff=datas.WordCount.substring(1, datas.WordCount.length-1).split(", " ).map(function(item) {
                return parseInt(item, 10);
            });
            for (var index = 0; index < jeff.length; index++) {
                newDataset.data.push(jeff[index]);
            }
            config.data.datasets.push(newDataset);
        }
        window.myLine.update();
    })
};

function getRandomColour() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function viewTable(UserID){
    let tableHTML = '<table>' +
        '<tr>' +
        '<th>Keyword</th>' +
        '</tr>';
    fetch("/datasets/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (var i=0; i<responseData.length; i++){
                tableHTML += `<tr>` +
                    `<td>${responseData[i].KeyWord}</td>` +
                    `</tr>`;
            }
            tableHTML += '</table>'
        }
        document.getElementById("tableHTML").innerHTML = tableHTML;

    })
}
function pageLoad(){
    checkLogin();
    document.getElementById("content").innerHTML = `<div class="genericDiv">` +
        `<input type="button" value="Filters" id="filtersButton" onclick="window.location.href = '/client/filters.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Run a Stream" id="streamButton" onclick="window.location.href = '/client/stream.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Search Archives" id="archivesButton" onclick="window.location.href = '/client/archives.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Process some data" id="processButton" onclick="window.location.href = '/client/process.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Settings" id="settingsButton" onclick="window.location.href = '/client/settings.html'">` +
        `</div>` +
        `<div class="genericDiv">` +
        `<input type="button" value="Admin Options" id="adminButton" onclick="window.location.href = '/client/admin.html'">` +
        `</div>`;
}


function checkLogin() {

    let username = Cookies.get("UserName");

    let logInHTML = '';

    if (username === undefined) {
        logInHTML = "Not logged in. <a href='/client/login.html'>Log in</a>";
    } else {
        logInHTML = "Logged in as " + username + ". <a href='/client/login.html?logout'>Log out</a>";

    }

    document.getElementById("loggedInDetails").innerHTML = logInHTML;

}