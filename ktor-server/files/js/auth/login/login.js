document.getElementById('login-form').addEventListener('submit', async function(event) {
        event.preventDefault(); // Prevent default form submission

        const api = window.api

        // Get form data
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // Prepare the payload (make sure it matches your required structure)
        const payload = {
            email: email,
            password: password
        };

        try {
            // Send POST request
            const data = await api.post(ApiClient.ENDPOINTS.LOGIN, payload);
            console.log('Response:', data);

            const token = data.data.token;
            // Store the token
            if (token) {
                localStorage.setItem('authToken', token);
                //store cookie
                //Store the token in a cookie (valid for 1 day)
                 const expirationDate = new Date();
                 expirationDate.setDate(expirationDate.getDate() + 10); // 1 day
                 document.cookie = `authToken=${token}; expires=${expirationDate.toUTCString()}; path=/`;
                 console.log('Token stored in a cookie as well:', document.cookie);
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