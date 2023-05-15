console.log('listEmployees' + JSON.stringify(listEmployees));
const labels = listEmployees.reduce(function(result, item) {
  result.push(item.name);
  return result;
}, []);

const randomColorGenerator = function () {
  return '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
};

function createDiagonalPattern(color = 'black') {
  let shape = document.createElement('canvas')
  shape.width = 10
  shape.height = 10
  let c = shape.getContext('2d')
  c.strokeStyle = color
  c.beginPath()
  c.moveTo(2, 0)
  c.lineTo(10, 8)
  c.stroke()
  c.beginPath()
  c.moveTo(0, 8)
  c.lineTo(2, 10)
  c.stroke()
  return c.createPattern(shape, 'repeat')
}

const projects = listEmployees.reduce(function(result, item) {
  item.employeeProjects.forEach(function(prj){
    const prjId = prj.project.id;
    if (!result[prjId]) {
      result[prjId] = {
        label: prj.project.name,
        data: [],
        backgroundColor: randomColorGenerator()
      };
    }
  });
  return result;
}, {});

listEmployees.forEach(function(item) {
  for (const prjId of Object.keys(projects)) {
    const prj = projects[prjId];
    const empPrjs = item.employeeProjects.filter(el => el.project.id === parseFloat(prjId));
    if (empPrjs.length) {
      const totalMonths = empPrjs.reduce((acc, curr) => acc + curr.employeeBookedMonths, 0);
      prj.data.push(totalMonths);
    } else {
      prj.data.push(0);
    }
  }
});

const horizontalArbitraryLine = {
    id: 'horizontalArbitraryLine',
    beforeDraw(chart, args, options) {
        const {ctx, chartArea: {top, right, bottom, left, width, height}, scales:
        {x, y} } = chart;
        ctx.save();
        ctx.strokeStyle = 'red';
        ctx.strokeRect(left, y.getPixelForValue(12), width, 1);
    }
};

const ctx = document.getElementById("myChart");
const myChart = new Chart(ctx, {
  type: 'bar',
  data: {
    labels: labels,
    datasets: Object.values(projects)
  },
  options: {
    scales: {
      x: {
        stacked: true
      },
      y: {
        stacked: true
      }
    }
  },
  plugins: [horizontalArbitraryLine]
});



