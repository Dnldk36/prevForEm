var inputs = document.getElementsByClassName('main-input');
var button = document.getElementById('confirm-button');

button.disabled = true;

for (var i = 0; i < inputs.length; i++) {
    inputs[i].addEventListener('input', function() {
        var allFilled = true;
        for (var j = 0; j < inputs.length; j++) {
            if (!inputs[j].value) {
                allFilled = false;
                break;
            }
        }
        button.disabled = !allFilled;
    });
}