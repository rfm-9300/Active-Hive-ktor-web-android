document.getElementById('submit-btn').addEventListener('click', async function(event) {
    event.preventDefault();

    // Get the auth token from localStorage
    const authToken = localStorage.getItem('authToken');
    if (!authToken) {
        console.error('No authentication token found');
        document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">Please log in to create an event.</p>`;
        return;
    }

    // Get form data
    const title = document.getElementById('title').value;
    const description = document.getElementById('description').value;
    const date = document.getElementById('date').value;
    const location = document.getElementById('location').value;

    // Prepare the payload
    const payload = {
        source: 'web',
        title: title,
        description: description,
        date: date,
        location: location
    };

    try {
        // Send POST request with Authorization header
        const response = await fetch('/events/create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${authToken}` // Add the auth token
            },
            body: JSON.stringify(payload)
        });

        // Handle unauthorized response
        if (response.status === 401) {
            // Token might be expired or invalid
            localStorage.removeItem('authToken'); // Clear the invalid token
            document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">Session expired. Please log in again.</p>`;
            // Optionally redirect to login page
            // window.location.href = '/login';
            return;
        }

        // Check if the request was successful
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse JSON response
        const data = await response.json();
        console.log('Response:', data);

        // Handle success
        if (data.message) {
            document.getElementById('event-content').innerHTML = `<p class="text-green-500 font-bold">Event Created Successfully!</p>`;
        } else {
            console.error('Failed to create the event:', data.message);
            document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">Failed to create the event: ${data.message}</p>`;
        }

    } catch (error) {
        console.error('Error during the event creation process:', error);
        document.getElementById('event-content').innerHTML = `<p class="text-red-500 font-bold">An error occurred while creating the event. Please try again later.</p>`;
    }
});