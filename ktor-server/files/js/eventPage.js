document.getElementById('submit-btn').addEventListener('click', function() {
    const formData = new FormData(document.forms['eventForm']);
    fetch('/events/create', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(html => {
        document.getElementById('event-content').innerHTML = html;
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    });
});
