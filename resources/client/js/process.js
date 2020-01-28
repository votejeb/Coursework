function pageLoad(){
    //view different relevant tables
    viewFilterTable(Cookies.get("userid"));
    viewDataTable(Cookies.get("userid"));
    // draw graph event listener
    document.getElementById("drawGraph").addEventListener("click", drawGraph);
}
window.value = checkList=["","a"];
var loadedFilters=[];
function drawGraph(event) {
//configuration constructor for graph
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
    };

    event.preventDefault();
    //creates formdata object
    const form = document.getElementById("graphForm");
    const formData = new FormData(form);
    //gets setid form formdata
    var tableID = formData.get("SetID");
    //fetches dataset information from the server to search through
    fetch("/datasets/listone/"+Cookies.get("userid"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData1=> {
        for (var index4 = 0; index4 < responseData1.length; index4++) {
            //fetches rawset data
            if (responseData1[index4].SetID===parseInt(tableID,10)){
                var RawSets=responseData1[index4].RawSets;
                //builds mychart object
                var ctx = document.getElementById('chartCanvas').getContext('2d');
                var myChart = new Chart(ctx, config);
                console.log(RawSets);
                //reads processed data table
                fetch("/processeddatas/readkeywords/"+tableID.toString()+"/"+RawSets, {method: 'get'}
                ).then(response => response.json()
                ).then(responseData => {
                    console.log(responseData);
                    for (var index5 = 0; index5 < checkList.length; index5++) {
                        for (var index6 = 0; index6 < responseData.length; index6++) {
                            if (checkList[index5]===responseData[index6].Words){
                                if (index6 > -1) {
                                    responseData.splice(index6, 1);
                                }
                                break;
                            }
                        }
                    }

                    console.log(responseData);
                    //converts string to array and iterates over each one to give x axis values
                    var RawSets1=RawSets.split("-");
                    for (var index3 = 0; index3 < RawSets1.length; index3++) {
                        config.data.labels.push(RawSets1[index3]);
                    }
                    //iterates over each value of json lis and tehm to table in teh correct order
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
                        //converts object to array
                        var jeff=datas.WordCount.substring(1, datas.WordCount.length-1).split(", " ).map(function(item) {
                            return parseInt(item, 10);
                        });
                        //pushes variable to config
                        for (var index = 0; index < jeff.length; index++) {
                            newDataset.data.push(jeff[index]);
                        }
                        //pushes new dataset
                        config.data.datasets.push(newDataset);
                    }
                    //updates chart instance
                    myChart.update();
                });
            } else {

            }
        }
    });

}
//random colour generator function
function getRandomColour() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}
//view dataset table function
function viewDataTable(UserID){
    //builds table layout
    let tableHTML = '<table>' +
        '<tr>' +
        '<th>SetID</th>' +
        '<th>|Keyword</th>' +
        '</tr>';
    //fetches data from dataset
    fetch("/datasets/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            //HTML constructor
            for (var i=0; i<responseData.length; i++){
                tableHTML += `<tr>` +
                    `<td>${responseData[i].SetID}</td>` +
                    `<td>|${responseData[i].KeyWord}</td>` +
                    `</tr>`;
            }
            tableHTML += '</table>'
        }
        document.getElementById("tableHTML").innerHTML = tableHTML;
    })
}
//view applicable filters to apply
function viewFilterTable(UserID){
    //builds table layout
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>Data Filter ID|</th>' +
        '<th>Data Filter Name|</th>' +
        '<th>Whitelist Status|</th>' +
        '<th>Public Status</th>' +
        '</tr>';
    //fetches data from dataset
    fetch("/datafilters/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (var i=0; i<responseData.length; i++){
                filterHTML += `<tr>` +
                    //HTML constructor
                    `<td>${responseData[i].DataFilterID}|</td>` +
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

function read1Filter(){
    debugger;
    const form = document.getElementById("viewFilterForm");
    const formData = new FormData(form);
    fetch("/linkedfilters/readfilter/"+formData.get("DataFilterID"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (var i=0; i<responseData.length; i++){
                checkList.append(responseData[i].Words);
            }
        }
        document.getElementById("viewFilterHTML").innerHTML = viewFilterHTML;
    })
}