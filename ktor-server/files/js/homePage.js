document.addEventListener('DOMContentLoaded', function() {
    // Try to get token from localStorage
    const token = localStorage.getItem('authToken');

    if (token) {
        fetch('/secret', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                // If token is invalid, remove it
                //localStorage.removeItem('auth_token');
                //window.location.href = '/login';
            }
            return response.json();
        })
        .then(data => {
            // Handle successful auth
            console.log('Authenticated:', data);
        })
        .catch(error => {
            console.error('Auth error:', error);
            //localStorage.removeItem('auth_token');
            //window.location.href = '/login';
        });
    } else {
        // No token found, redirect to login
        //window.location.href = '/login';
        console.log("No token found")
    }
});