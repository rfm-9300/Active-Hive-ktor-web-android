document.addEventListener('DOMContentLoaded', function() {
    if (typeof htmx === 'undefined') {
        console.error('HTMX is not loaded!');
        return;
    }

    const token = document.cookie.replace(/(?:(?:^|.*;\s*)authToken\s*=\s*([^;]*).*$)|^.*$/, "$1");
    console.log('Token:', token);
    if (token) {
        htmx.defineConfig({
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        console.log('HTMX headers configured successfully');
    } else {
        console.warn('No auth token found');
    }
});