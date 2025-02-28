let cropper;

function showEditProfile() {
  const editProfile = document.getElementById('floating-profile-page');

    if (editProfile.classList.contains('hidden')) {
        editProfile.classList.remove('hidden');
    } else {
        editProfile.classList.add('hidden');
    }
}

async function submitProfileEdit() {
    try {
        const api = window.api;
        if (!api.token) {
            showAlert('Please log in to edit your profile.', 'error');
            return;
        }
        // Create FormData object
        const formData = new FormData();

        // add cropped image
        if(cropper) {
            // Get the cropped image as BLOB
            const blob = await new Promise(resolve => {
                cropper.getCroppedCanvas().toBlob(resolve, 'image/jpeg', 0.9);
            });
            
            // Append as file with filename
            formData.append('image', blob, 'profile.jpg');
        }

        const data = await api.post(ApiClient.ENDPOINTS.UPDATE_PROFILE, formData, {}, false)
        console.log('Response:', data);

        if (!data.success){
            showAlert(data.message, 'error');
            return;
        }
        showEditProfile()
        showAlert('Profile updated successfully!', 'success');


    } catch (error) {
        console.error('Error during the event creation process:', error);
        document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">An error occurred while creating the event. Please try again later.</p>`;
    }
    
}

function loadcropper() {
    console.log('cropper LOADED');
    const imageInput = document.getElementById('image');
    const imagePreview = document.getElementById('image-preview');
    const cropperContainer = document.getElementById('image-cropper-container');
    

    imageInput.addEventListener('change', function (e) {
        const file = e.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (event) {
                // Show the image preview and Cropper.js container
                imagePreview.src = event.target.result;
                cropperContainer.classList.remove('hidden');

                // Initialize Cropper.js
                if (cropper) {
                    cropper.destroy(); // Destroy existing Cropper instance
                }
                cropper = new Cropper(imagePreview, {
                    aspectRatio: 1, // Set aspect ratio (e.g., 1:1 for square)
                    viewMode: 1, // Restrict the crop box to the image size
                    autoCropArea: 1, // Automatically crop the entire image
                });
            };
            reader.readAsDataURL(file);
        }
    });
};
loadcropper();