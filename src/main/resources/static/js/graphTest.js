console.log('listEmployees' + listEmployees);
const labels = listEmployees.reduce(function(result, item) {
  result.push(item.name);
  return result;
}, []);

const randomColorGenerator = function () {
  return '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
};

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



//Employee{
//id=7, name='Luke Sky', contractedFrom='2022-01-15', contractedTo='2025-04-30',
//employeeProjects=[EmployeeProject{id=24, project=Project{id=1, projectNumber=12345, name='Test Project', startDate='2022-02-01', endDate='2024-08-28', projectLengthInMonths=31.0, currentBookedMonths=41.5, remainingBookedMonths=-10.5, numberOfEmployees=0, comment='31 months'}, employeeBookedMonths=1.0, employeeProjectStartDate=2023-01-01, employeeProjectEndDate=2023-01-31, projectName='Test Project'},
//                  EmployeeProject{id=25, project=Project{id=2, projectNumber=54321, name='Test Project 2', startDate='2021-12-01', endDate='2023-02-28', projectLengthInMonths=15.0, currentBookedMonths=21.5, remainingBookedMonths=-6.5, numberOfEmployees=0, comment='Comment 15 months'}, employeeBookedMonths=0.5, employeeProjectStartDate=2023-04-01, employeeProjectEndDate=2023-04-14, projectName='Test Project 2'},
//                  EmployeeProject{id=27, project=Project{id=1, projectNumber=12345, name='Test Project', startDate='2022-02-01', endDate='2024-08-28', projectLengthInMonths=31.0, currentBookedMonths=41.5, remainingBookedMonths=-10.5, numberOfEmployees=0, comment='31 months'}, employeeBookedMonths=6.5, employeeProjectStartDate=2023-04-15, employeeProjectEndDate=2023-10-31, projectName='Test Project'},
//                  EmployeeProject{id=26, project=Project{id=3, projectNumber=2468, name='Test Project 3', startDate='2022-02-01', endDate='2025-04-30', projectLengthInMonths=39.0, currentBookedMonths=48.0, remainingBookedMonths=-9.0, numberOfEmployees=0, comment='Comment 39 months'}, employeeBookedMonths=1.5, employeeProjectStartDate=2023-11-15, employeeProjectEndDate=2023-12-31, projectName='Test Project 3'}], projectIds=null, startDates=null, endDates=null} but in const prj = projects[prjId];
