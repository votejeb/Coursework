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
    viewTable("1");
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
}

function addDataSet(datas,RawSets) {
    var newDataset = {
        label: [],
        backgroundColor: getRandomColour(),
        borderColor: getRandomColour(),
        data: [],
        fill: false
    };

    for (var index = 1; index < datas.length; ++index) {
        newDataset.data.push(datas[RawSets]);
    }
    config.data.datasets.push(newDataset);
    window.myLine.update();
}

function addData(fetchList){
    var fetcho = fetchList[config.data.labels.length % fetchList.length];
    config.data.labels.push(fetcho);
    config.data.datasets.forEach(function(dataset) {
        dataset.data.push();
    });
    window.myLine.update();
}

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