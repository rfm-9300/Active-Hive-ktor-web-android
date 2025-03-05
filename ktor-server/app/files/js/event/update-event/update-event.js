document.getElementById('submit-btn').addEventListener('click', async function(event) {
    console.log('submit-btn clicked');
    event.preventDefault();

    const api = window.api
    const contentDiv = document.getElementById('main-content');

    if (!api.token) {
        contentDiv.innerHTML = `<p class="text-red-500 font-bold">Please log in to create an event.</p>`;
        return;
    }

    try {
        // Create FormData object
        const formData = new FormData();

        // Add all form fields
        formData.append('eventId', document.getElementById('eventId').value);
        formData.append('title', document.getElementById('title').value);
        formData.append('description', document.getElementById('description').value);
        formData.append('date', document.getElementById('date').value);
        formData.append('location', document.getElementById('location').value);

        const data = await api.post(ApiClient.ENDPOINTS.UPDATE_EVENT, formData, {}, false)

        if (!data.success){
            contentDiv.innerHTML = `<p class="text-red-500 font-bold">An error occurred while creating the event. Please try again later.</p>`;
            console.error('Error during the event creation process:', response);
            return;
        }

        console.log('Response:', data);

        if (data.success) {
            contentDiv.innerHTML = `<p class="text-green-500 font-bold">Event Updated!</p>`;
        } else {
            contentDiv.innerHTML = `<p class="text-red-500 font-bold">An error occurred while updating the event. Please try again later.</p>`;
        }

    } catch (error) {
        console.error('Error during the event creation process:', error);
        document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">An error occurred while creating the event. Please try again later.</p>`;
    }
});

