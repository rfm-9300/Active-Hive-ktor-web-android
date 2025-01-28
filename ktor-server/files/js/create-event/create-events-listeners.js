console.log('main LOADED');

document.getElementById("image").addEventListener("change", function() {
  const fileInput = this;
  const file = fileInput.files[0];

  // Reference to the status element
  const uploadStatus = document.getElementById("upload-status");

  if (file) {
    // Update the text to show the filename
    const fileName = file.name.length > 10 ? file.name.substring(0, 10) + "..." : file.name;
    uploadStatus.textContent = `"${fileName}" selected!`;
  } else {
    // Clear the text if no file is selected
    uploadStatus.textContent = "";
  }
});

// Get the current date and time
const now = new Date();

// Format the date and time as YYYY-MM-DDTHH:MM
const year = now.getFullYear();
const month = String(now.getMonth() + 1).padStart(2, '0'); // Months are 0-based
const day = String(now.getDate()).padStart(2, '0');
const hours = String(now.getHours()).padStart(2, '0');
const minutes = String(now.getMinutes()).padStart(2, '0');

const formattedDate = `${year}-${month}-${day}T${hours}:${minutes}`;

// Set the value of the input
document.getElementById("date").value = formattedDate;