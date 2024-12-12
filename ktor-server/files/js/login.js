document.getElementById('login-form').addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevent default form submission

        // Get form data
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        // Prepare the payload (make sure it matches your required structure)
        const payload = {
            source: 'web',
            username: username,
            password: password
        };

        try {
            // Send POST request
            const response = await fetch('/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(payload) // Convert JS object to JSON string
            });

            // Check if the request was successful
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Parse JSON response
            const data = await response.json();
            console.log('Response:', data);

            // Store the token (assuming the server returns it as { token: '...' })
            if (data.token) {
                localStorage.setItem('authToken', data.token);
                console.log('Token stored in localStorage:', data.token);
            } else {
                console.error('No token found in response');
            }

            // Optionally update the page content (example)
            document.getElementById('main-content').innerHTML = 'Login Successful!';

        } catch (error) {
            console.error('Error during the login process:', error);
            document.getElementById('main-content').innerHTML = 'Login Failed. Please try again.';
        }
    });