async function submitPost() {
    try {
        const title = document.getElementById('post-title').value;
        const content = document.getElementById('post-content').value;

        const api = window.api;
        if (!api.token) {
            showAlert('Please log in to create a post.', 'error');
            return;
        }
        const payload = {
            title: title,
            content: content
        };
        console.log('payload', payload);

        // Send data as JSON
        const response = await api.post(ApiClient.ENDPOINTS.CREATE_POST, payload);

        if (!response.success) {
            showAlert(response.message, 'error');
            return;
        }

        showAlert('Post created successfully!', 'success');
    } catch (error) {
        showAlert('An error occurred while creating the post. Please try again later.', 'error');
    }
}