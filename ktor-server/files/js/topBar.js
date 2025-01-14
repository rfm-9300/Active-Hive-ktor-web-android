// Select elements
const logoContainer = document.getElementById('logo-container');
const dropdown = document.getElementById('logo-menu-dropdown');

// Toggle dropdown when clicking on the logo container
logoContainer.addEventListener('click', function (event) {
  event.stopPropagation(); // Prevent the click from propagating to the document
  dropdown.classList.toggle('hidden');
});

// Toggle dropdown when clicking on the profile container
profileContainer.addEventListener('click', function (event) {
  event.stopPropagation(); // Prevent the click from propagating to the document
  profileDropdown.classList.toggle('hidden');
});

// Close the dropdown when clicking anywhere outside the logoContainer or dropdown
document.addEventListener('click', function (event) {
  const isClickInside = logoContainer.contains(event.target) || dropdown.contains(event.target);
  if (!isClickInside) {
    dropdown.classList.add('hidden');
  }
});

// Close the dropdown when clicking anywhere outside the profileContainer or dropdown
document.addEventListener('click', function (event) {
  const isClickInside = profileContainer.contains(event.target) || profileDropdown.contains(event.target);
  if (!isClickInside) {
    profileDropdown.classList.add('hidden');
  }
});
