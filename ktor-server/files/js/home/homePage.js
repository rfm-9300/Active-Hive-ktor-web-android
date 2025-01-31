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

const contentDiv = document.getElementById('main-content');

//////////////
//Server sent events
///////

function setupSSE() {
    const eventSource = new EventSource('/home/sse');
    
    eventSource.onopen = (event) => {
        console.log('SSE Connection opened');
    };

    eventSource.addEventListener('like-update', (event) => {
        console.log('Like update:', event.data);
        // Handle like update here
    });

    eventSource.addEventListener('event-deleted', async (event) => {
        console.log('Event Delete:', event.data);
        // Handle event delete here
        const html = await window.api.getHtml('/home/events-tab');
        contentDiv.innerHTML = html;
    });

    eventSource.addEventListener('keepalive', (event) => {
        console.log('Keepalive received');
    });

    eventSource.addEventListener('error', (event) => {
        console.log('Error:', event.data);
    });

    eventSource.onerror = (event) => {
        if (eventSource.readyState === EventSource.CLOSED) {
            console.log('Connection closed, attempting to reconnect...');
            setTimeout(() => {
                eventSource.close();
                setupSSE();
            }, 5000); // Retry after 5 seconds
        }
    };

    return eventSource;
}

// Start the SSE connection
const sse = setupSSE();

// Clean up on page unload
window.addEventListener('beforeunload', () => {
    if (sse) {
        sse.close();
    }
});