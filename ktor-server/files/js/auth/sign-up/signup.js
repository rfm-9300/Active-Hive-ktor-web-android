document.getElementById('signup-form').addEventListener('submit', async function(event) {
    console.log('signup-form-form submitted');
        event.preventDefault(); // Prevent default form submission

        const api = window.api
        const contentDiv = document.getElementById('main-content');

        // Get form data
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            // Send POST request
            const data = await api.post(ApiClient.ENDPOINTS.SIGNUP, {email, password}, {})

            // Check if the request was successful
            if (!data.success) {
                console.log('HTTP error:', data.message);
            }

            console.log('Response:', data);


            // Optionally update the page content (example)
            contentDiv.innerHTML = 'Signup Successful!';

        } catch (error) {
            console.error('Error during the login process:', error);
            //document.getElementById('main-content').innerHTML = 'Signup Failed. Please try again. MESSAGE: ' + error.message;
        }
    });