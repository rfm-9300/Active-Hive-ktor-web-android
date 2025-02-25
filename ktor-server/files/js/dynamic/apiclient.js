class ApiClient {

    static ENDPOINTS = {
        CREATE_EVENT: '%%API_CREATE_EVENT%%',
        DELETE_EVENT: '%%API_DELETE_EVENT%%',
        UPDATE_EVENT: '%%API_UPDATE_EVENT%%',
        SSE_CONNECTION: '%%SSE_CONNECTION%%',
        DELETE_POST: '%%API_DELETE_POST%%',
        LOGIN: '%%API_LOGIN%%',
        SIGNUP: '%%API_SIGNUP%%',
        JOIN_EVENT: '%%API_JOIN_EVENT%%',
        PROFILE_MENU: '%%PROFILE_MENU%%',
        EVENTS_TAB: '%%EVENTS_TAB%%',
        HOME_PAGE: '%%UI_HOME%%',
        UPDATE_PROFILE: '%%API_UPDATE_PROFILE%%'
    }

    constructor(baseURL = '') {
        this.baseURL = baseURL;
        this.token = document.cookie.replace(/(?:(?:^|.*;\s*)authToken\s*=\s*([^;]*).*$)|^.*$/, "$1");
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
    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`;
        const headers = this.getHeaders(options.headers);
    
        try {
            const response = await fetch(url, {
                ...options,
                headers
            });
    
            if (!response.ok) {
                console.log('Response Error:', response);
                return { success: false, message: 'Request failed' };
            }
            
            try {
                const data = await response.json(); // Attempt to parse the response text as JSON
                console.log('Response Data:', data);
                return data;
            } catch (parseError) {
                console.error('Failed to parse JSON:', parseError);
                throw new Error('Invalid JSON response from server');
            }
        } catch (error) {
            console.error('Request failed:', error);
            throw error;
        }
    }

    async getHtml(endpoint) {
        return await fetch(endpoint, {
            headers: this.getHeaders({
                'Accept': 'text/html',
                'Content-Type': 'text/html'
            }),
            method: 'GET'
        }).then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.text();
        }).catch(error => {
            console.error('Error fetching HTML:', error);
            throw error;
        });
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