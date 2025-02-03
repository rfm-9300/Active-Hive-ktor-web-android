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
                console.error('HTTP error:', response.message);
            }

            // Parse JSON response
            const data = await response.json();
            console.log('Response:', data);

            // Store the token
            if (data.token) {
                localStorage.setItem('authToken', data.token);
                //store cookie
                //Store the token in a cookie (valid for 1 day)
                 const expirationDate = new Date();
                 expirationDate.setDate(expirationDate.getDate() + 10); // 1 day
                 document.cookie = `authToken=${data.token}; expires=${expirationDate.toUTCString()}; path=/`;
                 console.log('Token stored in a cookie as well:', document.cookie);
                console.log('Token stored in localStorage:', data.token);

                // Redirect to the home page
                window.location.href = '/';
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