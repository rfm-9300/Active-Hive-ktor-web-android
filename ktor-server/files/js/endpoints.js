class ApiClient {
    constructor(baseURL = '') {
        this.baseURL = baseURL;
        this.token = localStorage.getItem('authToken');
    }

    // Updates the token
    setToken(newToken) {
        this.token = newToken;
        localStorage.setItem('authToken', newToken);
    }

    // Removes the token
    clearToken() {
        this.token = null;
        localStorage.removeItem('authToken');
    }

    // Creates default headers with optional additional headers
    getHeaders(additionalHeaders = {}) {
        const headers = {
            'Content-Type': 'application/json',
            ...additionalHeaders
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return headers;
    }

    // Add this method to the ApiClient class
    async getHtml(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const headers = this.getHeaders(options.headers);

        try {
            const response = await fetch(url, {
                ...options,
                method: 'GET',
                headers
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            return response.text();
        } catch (error) {
            console.error('Request failed:', error);
            throw error;
        }
    }

    

    // Generic request method
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const headers = this.getHeaders(options.headers);

        try {
            const response = await fetch(url, {
                ...options,
                headers
            });

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Request failed:', error);
            throw error;
        }
    }

    // GET request
    async get(endpoint, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'GET'
        });
    }

    // POST request
    async post(endpoint, data, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'POST',
            body: JSON.stringify(data)
        });
    }

    // PUT request
    async put(endpoint, data, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'PUT',
            body: JSON.stringify(data)
        });
    }

    // DELETE request
    async delete(endpoint, options = {}) {
        return this.request(endpoint, {
            ...options,
            method: 'DELETE'
        });
    }
}

// Make it global
window.ApiClient = ApiClient;