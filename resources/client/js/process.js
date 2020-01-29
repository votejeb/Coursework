function pageLoad(){
    //view different relevant tables
    viewFilterTable(Cookies.get("userid"));
    viewDataTable(Cookies.get("userid"));
    // draw graph event listener
    document.getElementById("drawGraph").addEventListener("click", drawGraph);
    document.getElementById("removeFilter").addEventListener("click", remove1Filter);
    document.getElementById("addFilter").addEventListener("click", add1Filter);
}
//global variables to allow filters to work correctly
window.value = checkList=[];
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
    const tableID = formData.get("SetID");
    //fetches dataset information from the server to search through
    fetch("/datasets/listone/"+Cookies.get("userid"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData1=> {
        for (let index4 = 0; index4 < responseData1.length; index4++) {
            //fetches rawset data
            if (responseData1[index4].SetID===parseInt(tableID,10)){
                var RawSets=responseData1[index4].RawSets;
                //builds mychart canvas object
                const ctx = document.getElementById('chartCanvas').getContext('2d');
                const myChart = new Chart(ctx, config);
                console.log(RawSets);
                //reads processed data table
                fetch("/processeddatas/readkeywords/"+tableID.toString()+"/"+RawSets, {method: 'get'}
                ).then(response => response.json()
                ).then(responseData => {
                    //responsedata log for bugfixing and troubleshooting
                    console.log(responseData);
                    //filter checker and remover-possibly could be more efficient
                    for (let index5 = 0; index5 < checkList.length; index5++) {
                        for (let index6 = 0; index6 < responseData.length; index6++) {
                            if (checkList[index5]===responseData[index6].Words){
                                if (index6 > -1) {
                                    responseData.splice(index6, 1);
                                }
                                break;
                            }
                        }
                    }
                    //responsedata log for troubleshooing
                    console.log(responseData);
                    //converts string to array and iterates over each one to give x axis values
                    var RawSets1=RawSets.split("-");
                    for (let index3 = 0; index3 < RawSets1.length; index3++) {
                        config.data.labels.push(RawSets1[index3]);
                    }
                    //iterates over each value of json list and them to table in teh correct order
                    for (let index2 = 0; index2 < responseData.length; index2++) {
                        const datas = responseData[index2];
                        //new dataset constructor
                        const newDataset = {
                            label: [],
                            backgroundColor: getRandomColour(),
                            borderColor: getRandomColour(),
                            data: [],
                            fill: false
                        };
                        //pushes label to new set
                        newDataset.label.push(datas.Words);
                        //converts javascript object string to array
                        const je = datas.WordCount.substring(1, datas.WordCount.length - 1).split(", ").map(function (item) {
                            return parseInt(item, 10);
                        });
                        //pushes variable to config
                        for (let index = 0; index < je.length; index++) {
                            newDataset.data.push(je[index]);
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
    for (let i = 0; i < 6; i++) {
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
            for (let i=0; i<responseData.length; i++){
                tableHTML += `<tr>` +
                    `<td>${responseData[i].SetID}</td>` +
                    `<td>|${responseData[i].KeyWord}</td>` +
                    `</tr>`;
            }
            tableHTML += '</table>'
        }
        //html assigner
        document.getElementById("tableHTML").innerHTML = tableHTML;
    })
}
//view applicable filters to apply
function viewFilterTable(UserID){
    //builds table layout
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>Data Filter ID</th>' +
        '<th>|Data Filter Name</th>' +
        '<th>|Whitelist Status</th>' +
        '<th>|Public Status</th>' +
        '</tr>';
    //fetches data from dataset
    fetch("/datafilters/listone/"+UserID,{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (let i=0; i<responseData.length; i++){
                filterHTML += `<tr>` +
                    //HTML constructor
                    `<td>${responseData[i].DataFilterID}</td>` +
                    `<td>|${responseData[i].DataFilterName}</td>` +
                    `<td>|${responseData[i].WhitelistBlacklist}</td>` +
                    `<td>|${responseData[i].PublicPrivate}</td>` +
                    `</tr>`;
            }
            filterHTML += '</table>'
        }
        //html assigner
        document.getElementById("filterHTML").innerHTML = filterHTML;
    })
}

function add1Filter(event){
    event.preventDefault();
    //form constructor
    const form = document.getElementById("filterForm");
    const formData = new FormData(form);
    //calls filter data
    fetch("/linkedfilters/readfilter/"+formData.get("DataFilterID"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        //responsedata log for bugfixing
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            for (let i=0; i<responseData.length; i++){
                //pushes response to global var
                checkList.push(responseData[i].Words);
            }
        }
    });
    //console logger for bugfixing purposes
    console.log(checkList);
}

function remove1Filter(event){
    event.preventDefault();
    //formdata constructor
    const form = document.getElementById("filterForm");
    const formData = new FormData(form);
    fetch("/linkedfilters/readfilter/"+formData.get("DataFilterID"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        console.log(responseData);
        if (responseData.hasOwnProperty('error')) {
            alert(responseData.error);
        } else {
            //removes words from filter list
            for (let i=0; i<responseData.length; i++){
                if (i > -1) {
                    checkList.splice(responseData[i].Words, 1);
                }
            }
        }
    });
    //console logger for bugfixing
    console.log(checkList);
}