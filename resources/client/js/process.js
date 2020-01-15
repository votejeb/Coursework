
function drawgraph() {

    fetch("/processeddatas/readkeywords/1", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {

        const canvas = document.getElementById('chartCanvas');
        const context = canvas.getContext('2d');

        var arr = Object.keys(responseData).map((key) => [key, responseData[key]]);

        console.log(arr);

        let myChart = new Chart(context, {
            type: 'line',
            data: {
                labels: ['1'],
            },
            options: {
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        }
                    }]
                },
                responsive: false
            }
        });
        adddata(arr);
    })
}

function adddata(myChart,arr) {
    var i;
    for (i = 0; i < arr.length; i++) {
        var j;
        for (j = 0; j < arr[i].length; i++) {
            myChart.data.datasets.push({
                label: arr[i][j],
                backgroundColor: getRandomColour(),
                data: arr[i][j]
            });
            myChart.update();
        }
    }
}
function getRandomColour() {
    var letters = '0123456789ABCDEF';
    var color = '#';
    for (var i = 0; i < 6; i++) {
        color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
}