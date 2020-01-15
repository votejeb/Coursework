
function drawgraph() {

    fetch("/processeddatas/readkeywords/1", {method: 'get'}
    ).then(response => response.json()
    ).then(responseData => {

        const canvas = document.getElementById('chartCanvas');
        const context = canvas.getContext('2d');

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
        adddata(myChart,responseData);
    })
}

function adddata(myChart,arr) {
    var i;
    for (i = 0; i < arr.length; i++) {
        myChart.data.datasets.push({
            label: arr[i].Words,
            data: [arr[i].WordCount],
            backgroundColor: getRandomColour()
        });
        myChart.update();
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