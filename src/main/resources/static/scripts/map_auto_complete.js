function initMap() {
    var input = document.getElementById('destination');
    new google.maps.places.Autocomplete(input, ['cities']);
    // var input = document.getElementById('inputId');
    new google.maps.event.addDomListener(input, 'keydown', function(event) {
        if (event.keyCode === 13) {
            event.preventDefault();
        }
    });
}
