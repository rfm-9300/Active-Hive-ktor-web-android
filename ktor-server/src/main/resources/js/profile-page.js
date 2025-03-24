function switchTab(tabName) {
    // Hide all content
    document.getElementById('hosted-content').classList.add('hidden');
    document.getElementById('attended-content').classList.add('hidden');
    document.getElementById('waiting-content').classList.add('hidden');
    document.getElementById('attending-content').classList.add('hidden');

    // Remove active state from all tabs
    document.getElementById('hosted-tab').classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
    document.getElementById('attended-tab').classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
    document.getElementById('waiting-tab').classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');
    document.getElementById('attending-tab').classList.remove('text-blue-600', 'border-b-2', 'border-blue-600');

    // Add default style to all tabs
    document.getElementById('hosted-tab').classList.add('text-gray-500');
    document.getElementById('attended-tab').classList.add('text-gray-500');
    document.getElementById('waiting-tab').classList.add('text-gray-500');
    document.getElementById('attending-tab').classList.add('text-gray-500');

    // Show selected content and activate tab
    document.getElementById(`${tabName}-content`).classList.remove('hidden');
    document.getElementById(`${tabName}-tab`).classList.remove('text-gray-500');
    document.getElementById(`${tabName}-tab`).classList.add('text-blue-600', 'border-b-2', 'border-blue-600');
}

// Show hosted events by default when page loads
document.addEventListener('DOMContentLoaded', () => {
    switchTab('hosted');
}); 