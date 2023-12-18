function testRedirect(id){
    window.location.href='http://localhost:8081/test/'+id;
}

function bookRedirect(id) {
    window.location.href='http://localhost:8081/main/'+id;
}

//предосмотр картинки
function loadFile(event) {
    var output = document.getElementById('output');
    output.src = URL.createObjectURL(event.target.files[0]);
    output.onload = function() {
        URL.revokeObjectURL(output.src) // free memory
    }
};

function myFunction(){
    var x = document.getElementById("bookFile").value;
    document.getElementById("demo").innerHTML = ' Uploaded file name: ' + x;
}
