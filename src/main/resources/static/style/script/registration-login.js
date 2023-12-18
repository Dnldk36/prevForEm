var inputs = document.getElementsByClassName('reg-input');
var warning = document.getElementById('warning');

for (var i = 0; i < inputs.length; i++) {
    inputs[i].addEventListener('blur', function(event) {
        if (!event.target.value) {
            event.target.style.borderBottom = '2px solid #951E41';
            warning.style.color = '#951E41';
        } else {
            event.target.style.borderBottom = '';
            var allFilled = true;
            for (var j = 0; j < inputs.length; j++) {
                if (!inputs[j].value) {
                    allFilled = false;
                    break;
                }
            }
            if (allFilled) {
                warning.style.color = '';
            }
        }
    });
}