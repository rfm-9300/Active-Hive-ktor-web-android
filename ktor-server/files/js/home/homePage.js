document.addEventListener('DOMContentLoaded', async function() {
    const api = new ApiClient();
    const loginButton = document.getElementById('login-button');

    if (api.token) {
        try {
            const html = await api.getHtml('/home/user-info');
            if (!html) {
                throw new Error('No HTML received');
            }

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
            loginButton.classList.add('hidden');

        } catch (error) {
            console.log('Error:', error);
            loginButton.classList.remove('hidden');
        }
    } else {
        console.log("No token found");
        loginButton.classList.remove('hidden');
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
const contentDiv = document.getElementById('main-content');
// Listen to all events
eventSource.onmessage = (event) => {
    console.log('sse message:', event.data);
    if (event.data === 'refresh-events') {
        console.log('Received like update');
        const html = api.getHtml('/home/events-tab');
        contentDiv.innerHTML = html;
    }
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