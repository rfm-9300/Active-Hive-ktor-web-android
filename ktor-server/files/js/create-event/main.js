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