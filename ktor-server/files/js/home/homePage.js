document.addEventListener('DOMContentLoaded', async function() {
    const api = window.api;
    const loginButton = document.getElementById('login-button');
    const logoutButton = document.getElementById('logout-button');

    if (api.token) {
        try {
            const html = await api.getHtml(ApiClient.ENDPOINTS.PROFILE_MENU);
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

            // show logout button
            logoutButton.classList.remove('hidden');

        } catch (error) {
            console.log('Error:', error);
            loginButton.classList.remove('hidden');
            logoutButton.classList.add('hidden');
        }
    } else {
        console.log("No token found");
        loginButton.classList.remove('hidden');
        logoutButton.classList.add('hidden');
    }
});

// function for logout button
function logout() {
    console.log('Logging out...');
    localStorage.removeItem('authToken');
    document.cookie = 'authToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
    window.location.href = '/';
}

async function deletePost(postId) {
    console.log('Deleting post:', postId);
    const api = window.api;

    const data = await api.post(ApiClient.ENDPOINTS.DELETE_POST, { postId: postId });
    
    if (data.success) {
        console.log('Post deleted:', postId);
        showAlert('Post deleted', 'success');
    } else {
        console.log('Error deleting post:', data.message);
        showAlert(data.message, 'error');
    }
}