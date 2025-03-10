async function joinEvent(eventId) {
    
    const data = await window.api.post(ApiClient.ENDPOINTS.JOIN_EVENT, { eventId: eventId });
    console.log('Event joined:', data.success);
    // Optionally update the page content (example)
    if (data.success) {
        const message = data.message;
        // Example: Show a success message
        showAlert(message, "success");
    } else {
        const message = data.message;
        // Example: Show an error message
        showAlert(message, "error");
    }
}

function updateCountdown() {
    console.log('Updating countdown...');
    console.log('Event date:', eventDate);
    const now = new Date();
    const eventTime = new Date(eventDate);
    const difference = eventTime - now;
    if (difference <= 0) {
        document.getElementById("countdown").innerHTML = "Event has started!";
    } else {
        const days = Math.floor(difference / (1000 * 60 * 60 * 24));
        const hours = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((difference % (1000 * 60)) / 1000);
        document.getElementById("countdown").innerHTML = `${days} days, ${hours} hours, ${minutes} minutes, ${seconds} seconds`;
    }
}

function toggleWaitingList() {
    const content = document.getElementById('waiting-list');
    const icon = document.getElementById('toggle-icon');
    content.classList.toggle('hidden');
    if (content.classList.contains('hidden')) {
        icon.setAttribute('d', 'M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z'); // Down arrow
    } else {
        icon.setAttribute('d', 'M14.707 12.707a1 1 0 01-1.414 0L10 9.414l-3.293 3.293a1 1 0 01-1.414-1.414l4-4a1 1 0 011.414 0l4 4a1 1 0 010 1.414z'); // Up arrow
    }
}

setInterval(updateCountdown, 1000);
updateCountdown(); // Initial call
