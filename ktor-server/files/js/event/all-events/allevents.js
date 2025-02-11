function deleteEvent(eventId) {
    window.api.post(ApiClient.ENDPOINTS.DELETE_EVENT, { eventId })
        .then(data => {
            console.log('Event deleted:', eventId);
        })
        .catch(error => console.error('Error deleting event:', error));
}

function updateEvent(eventId) {
    window.api.post(ApiClient.ENDPOINTS.UPDATE_EVENT, { eventId })
        .then(data => {
            console.log('Event updated:', eventId);
            
        })
        .catch(error => console.error('Error updating event:', error));
}

