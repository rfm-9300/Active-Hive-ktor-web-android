async function joinEvent(eventId) {
    
    const data = await window.api.post(ApiClient.ENDPOINTS.JOIN_EVENT, { eventId: eventId });
    console.log('Event joined:', data.success);
    // Optionally update the page content (example)
    if (data.success) {
        const message = data.message;
        // Example: Show a success message
        showAlert("Login successful  teste!", "success");
    }else{
        const message = data.message;
        // Example: Show an error message
        showAlert("Login failed teste!", "error");
    }
}