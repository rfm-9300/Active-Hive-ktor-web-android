// Select the element with ID 'logo-container'
const logoContainer = document.getElementById('logo-container');

function toggleDropdown() {
        const dropdown = document.getElementById('dropdown');
        dropdown.classList.toggle('hidden'); // Toggle the 'hidden' class
    }

// Attach the click event listener
logoContainer.addEventListener('click', toggleDropdown);