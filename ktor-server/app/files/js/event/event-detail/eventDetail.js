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

setInterval(updateCountdown, 1000);
updateCountdown(); // Initial call
