class ApiClient {

    static ENDPOINTS = {
        LOGIN: '/auth/login',
        REGISTER: '/auth/register',
        EVENTS: '/events',
        EVENT: '/events/:id',
        CREATE_EVENT: '/events/create',
        DELETE_EVENT: '/events/:id/delete',
        UPDATE_EVENT: '/events/:id/update',
        RSVP: '/events/:id/rsvp',
        CANCEL_RSVP: '/events/:id/cancel-rsvp',
        PROFILE: '/profile',
        PROFILE_UPDATE: '/profile/update',
        PROFILE_IMAGE: '/profile/image',
        PROFILE_PASSWORD: '/profile/password',
        PROFILE_DELETE: '/profile/delete',
        LOGOUT: '/auth/logout'
    }

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

            console.log('Response:', response);

            if(!response.ok) {
                const error = await response.json();
                throw error;
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
            body: data
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