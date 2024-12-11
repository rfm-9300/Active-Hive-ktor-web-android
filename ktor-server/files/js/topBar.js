// Select elements
const logoContainer = document.getElementById('logo-container');
const dropdown = document.getElementById('dropdown');

// Toggle dropdown when clicking on the logo container
logoContainer.addEventListener('click', function (event) {
  event.stopPropagation(); // Prevent the click from propagating to the document
  dropdown.classList.toggle('hidden');
});

// Close the dropdown when clicking anywhere outside the logoContainer or dropdown
document.addEventListener('click', function (event) {
  const isClickInside = logoContainer.contains(event.target) || dropdown.contains(event.target);
  if (!isClickInside) {
    dropdown.classList.add('hidden');
  }
});
