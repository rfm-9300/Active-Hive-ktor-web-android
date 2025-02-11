//////////////
//Server sent events
///////

const SseActionTypes = {
    SSE_ACTION: 'sse-action',
    REFRESH_EVENTS: 'refresh-events',
};

function setupSSE() {
    const eventSource = new EventSource(ApiClient.ENDPOINTS.SSE_CONNECTION);
    const contentDiv = document.getElementById('main-content');
    
    eventSource.onopen = (event) => {
        console.log('SSE Connection opened');
    };

    eventSource.addEventListener(SseActionTypes.SSE_ACTION, async (event) => {
        console.log('Event Delete:', event.data);
        // Handle event delete here
        const data = event.data;

        if (data === SseActionTypes.REFRESH_EVENTS) {
            console.log('Refreshing events tab');
            const html = await window.api.getHtml('/home/events-tab');
            contentDiv.innerHTML = html;
        }
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