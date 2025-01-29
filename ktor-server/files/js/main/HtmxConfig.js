document.addEventListener('DOMContentLoaded', function() {
    try {
        const token = localStorage.getItem('authToken');
        if (token) {
            htmx.config.headers = {
                'Authorization': 'Bearer ' + token,
                'X-User': 'rfm-9300'
            };
            console.log('HTMX headers configured successfully');
        } else {
            console.warn('No auth token found');
        }
    } catch (error) {
        console.error('Error configuring HTMX headers:', error);
    }
});