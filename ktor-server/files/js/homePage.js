document.addEventListener('DOMContentLoaded', function() {
    const token = localStorage.getItem('authToken');

    if (token) {
        fetch('/home/user-info', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.text();
        })
        .then(html => {
            const tempDiv = document.createElement('div');
            tempDiv.innerHTML = html;

             // If there's no body tag, use the entire HTML content
            const bodyContent = tempDiv.querySelector('body') ?
            tempDiv.querySelector('body').innerHTML :
            tempDiv.innerHTML;

            const targetDiv = document.getElementById('user-profile-icon');
            targetDiv.innerHTML = bodyContent;
        })
        .catch(error => {
            console.error('Error:', error);
            // Don't swap HTML on error
            // const targetDiv = document.getElementById('user-profile-icon');
            // targetDiv.innerHTML = '<p>Error loading user info</p>';
        });
    } else {
        console.log("No token found");
        // const targetDiv = document.getElementById('user-profile-icon');
        // targetDiv.innerHTML = '<p>Please log in</p>';
    }
});