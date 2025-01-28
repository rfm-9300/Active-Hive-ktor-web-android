document.addEventListener('DOMContentLoaded', async function() {
    const api = new ApiClient();

    if (api.token) {
        try {
            const html = await api.getHtml('/home/user-info');

            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

            const bodyContent = tempDiv.querySelector('body') ?
                tempDiv.querySelector('body').innerHTML :
                tempDiv.innerHTML;

            const targetDiv = document.getElementById('user-profile-icon');
            targetDiv.innerHTML = bodyContent;

            // Add events
            const profileContainer = document.getElementById('profile-container');
            const profileDropdown = document.getElementById('profile-menu-dropdown');

            profileContainer.addEventListener('click', function(event) {
                event.stopPropagation();
                profileDropdown.classList.toggle('hidden');
            });

            document.addEventListener('click', function() {
                profileDropdown.classList.add('hidden');
            });

            // hide login button
            document.getElementById('login-button').classList.add('hidden');

        } catch (error) {
            console.error('Error:', error);
        }
    } else {
        console.log("No token found");
    }
});

//////////////
//Server sent events
///////

// Create connection to SSE endpoint
const eventSource = new EventSource('/home/sse');

// Listen to specific event type 'data-test'
/* eventSource.addEventListener('like-update', (event) => {
    console.log('Received data:', event.data);
    // Handle the data here
});*/

// Listen to all events
eventSource.onmessage = (event) => {
    console.log('sse message:', event.data);
};

// Handle connection open
eventSource.onopen = (event) => {
    console.log('Connection opened');
};

// Handle errors
eventSource.onerror = (event) => {
    if (eventSource.readyState === EventSource.CLOSED) {
        console.log('Connection closed');
    } else {
        console.error('Error occurred:', event);
    }
};