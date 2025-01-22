document.getElementById('submit-btn').addEventListener('click', async function(event) {
console.log('submit-btn clicked');
    event.preventDefault();

    const authToken = localStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
        document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">Please log in to create an event.</p>`;
        return;
    }

    try {
        // Create FormData object
        const formData = new FormData();

        // Add all form fields
        formData.append('source', 'web');
        formData.append('title', document.getElementById('title').value);
        formData.append('description', document.getElementById('description').value);
        formData.append('date', document.getElementById('date').value);
        formData.append('location', document.getElementById('location').value);

        // Add image file if it exists
        const fileInput = document.getElementById('image');
        if (fileInput.files[0]) {
            formData.append('image', fileInput.files[0]);
        }

        const response = await fetch('/events/create', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${authToken}`
                // Don't set Content-Type - browser will set it automatically with boundary
            },
            body: formData
        });

        if (response.status === 401) {
            localStorage.removeItem('authToken');
            document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">Session expired. Please log in again.</p>`;
            return;
        }

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Response:', data);

        if (data.success) {
            document.getElementById('event-content').innerHTML = `<p class="text-green-500 font-bold">Event Created Successfully!</p>`;
            setTimeout(() => {
                window.location.href = '/events';
            }, 2000);
        } else {
            throw new Error(data.message || 'Failed to create event');
        }

    } catch (error) {
        console.error('Error during the event creation process:', error);
        //document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">An error occurred while creating the event. Please try again later.</p>`;
    }
});