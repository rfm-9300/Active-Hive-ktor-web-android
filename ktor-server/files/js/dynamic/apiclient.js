class ApiClient {

    static ENDPOINTS = {
        CREATE_EVENT: '%%API_CREATE_EVENT%%',
        DELETE_EVENT: '%%API_DELETE_EVENT%%',
        UPDATE_EVENT: '%%API_UPDATE_EVENT%%',
        SSE_CONNECTION: '%%SSE_CONNECTION%%'
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
    async post(endpoint, data, options = {}, useJsonHeaders = true) {
        const headers = useJsonHeaders 
            ? { 'Content-Type': 'application/json', ...options.headers }
            : options.headers;
     
        return this.request(endpoint, {
            ...options,
            method: 'POST',
            headers,
            body: useJsonHeaders ? JSON.stringify(data) : data
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
window.api = new ApiClient();
window.contentDiv = document.getElementById('main-content');