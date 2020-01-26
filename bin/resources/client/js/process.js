function pageLoad(){
    viewDataTable(Cookies.get("userid"));
    document.getElementById("drawGraph").addEventListener("click", drawGraph);
}

function drawGraph(event) {

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

    const form = document.getElementById("graphForm");
    const formData = new FormData(form);

    var tableID = formData.get("SetID");

    fetch("/datasets/listone/"+Cookies.get("userid"),{method:'get'}
    ).then(response=>response.json()
    ).then(responseData=> {
        for (var index4 = 0; index4 < responseData.length; index4++) {
            if (responseData[index4].SetID===parseInt(tableID,10)){
                var RawSets=responseData[index4].RawSets;

                var ctx = document.getElementById('chartCanvas').getContext('2d');
                var myChart = new Chart(ctx, config);
                console.log(RawSets);
                fetch("/processeddatas/readkeywords/"+tableID.toString()+"/"+RawSets, {method: 'get'}
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
                    myChart.update();
                });
            } else {

            }
        }
    });

}

function getRandomColour() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}

function viewDataTable(UserID){
    let tableHTML = '<table>' +
        '<tr>' +
        '<th>SetID</th>' +
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
                    `<td>${responseData[i].SetID}</td>` +
                    `<td>${responseData[i].KeyWord}</td>` +
                    `</tr>`;
            }
            tableHTML += '</table>'
        }
        document.getElementById("tableHTML").innerHTML = tableHTML;
    })
}

function viewFilterTable(UserID){
    let filterHTML = '<table>' +
        '<tr>' +
        '<th>SetID</th>' +
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
                filterHTML += `<tr>` +
                    `<td>${responseData[i].SetID}</td>` +
                    `<td>${responseData[i].KeyWord}</td>` +
                    `</tr>`;
            }
            filterHTML += '</table>'
        }
        document.getElementById("filterHTML").innerHTML = filterHTML;
    })
}