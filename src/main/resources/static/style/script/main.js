let links = document.querySelectorAll(".menu > .menu-item");
for (let i = 0; i < links.length; i++) {
    links[i].onclick = function () {
        document.getElementById(links[i].getAttribute("data-link")).scrollIntoView({behavior: "smooth"});
    }
}

function goToSingIn() {
    window.location.href = 'http://localhost:8081/home'
}

function goToSingUp() {
    window.location.href = 'http://localhost:8081/registration'
}