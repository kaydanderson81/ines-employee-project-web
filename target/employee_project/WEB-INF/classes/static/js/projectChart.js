console.log("List of projects: " + listProjects)
const projectLabels = listProjects.reduce(function(result, item) {
  result.push(item.name);
  return result;
}, []);

    const projectLengthInMonths = listProjects.reduce(function(result, item) {
      result.push(item.projectLengthInMonths);
      return result;
    }, []);

//const projects = listProjects.reduce(function(result, item) {
//  item.forEach(function(prj){
//    const prjId = prj.project.id;
//    if (!result[prjId]) {
//      result[prjId] = {
//        label: prj.project.name,
//        data: [],
//        backgroundColor: randomColorGenerator()
//      };
//    }
//  });
//  return result;
//}, {});

const projectCurrentBookedMonths = listProjects.reduce(function(result, item) {
  result.push(item.currentBookedMonths);
  return result;
}, []);

const colorGenerator = function () {
  return '#' + (Math.random().toString(16) + '0000000').slice(2, 8);
};

const graph = document.getElementById('myBarChart');
console.log(listProjects)
  new Chart(graph, {
    type: 'bar',
    data: {
      labels: projectLabels,
      datasets: [
      {
          label: 'Currently booked',
          data: projectCurrentBookedMonths,
          borderWidth: 1,
          barPercentage: 0.5,
          backgroundColor: colorGenerator()
        },{
                  label: 'Length in Months',
                  data: projectLengthInMonths,
                  borderWidth: 1,
                  backgroundColor: colorGenerator()
                }]
    },
    options: {
      scales: {
        x: {
            stacked: true
        },
        y: {
          beginAtZero: true

        },

      }
    }
  });