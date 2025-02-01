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

