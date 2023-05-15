
//Accordion
var acc = document.getElementsByClassName("accordion");
var i;

for (i = 0; i < acc.length; i++) {
  acc[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var panel = this.nextElementSibling;
    if (panel.style.maxHeight) {
      panel.style.maxHeight = null;
    } else {
      panel.style.maxHeight = panel.scrollHeight + "px";
    }
  });
}

document.addEventListener('DOMContentLoaded', function() {
  const urlParams = new URLSearchParams(window.location.search);
  const updatedEmployeeId = urlParams.get('updatedEmployeeId');

  const employeeId = 'employee-' + updatedEmployeeId;
  if (updatedEmployeeId) {
    const accordionButton = document.getElementById(`employee-${updatedEmployeeId}`);
    if (accordionButton) {
      // Add the "active" class to the button
      accordionButton.classList.add('active');

      // Expand the corresponding accordion panel
      const accordionPanel = accordionButton.nextElementSibling;
      accordionPanel.style.maxHeight = accordionPanel.scrollHeight + "px";

      // Scroll to the accordion
      setTimeout(() => {
        accordionButton.scrollIntoView({ behavior: 'smooth', block: 'start' });
      }, 500);
    }
  }
});





