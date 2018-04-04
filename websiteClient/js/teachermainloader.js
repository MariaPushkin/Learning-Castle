window.onload = function () {
    //var u = new Irl(window.location);
    var mainpage = document.getElementById("mainpage");
    var courcespage = document.getElementById("courcespage");
    mainpage.href = "teacherMain.html" + window.location.search;
    courcespage.href ="teacherCources.html" + window.location.search;
    console.log(courcespage.href);
    //console.log(u.id);

    var socket = new WebSocket('ws://127.0.0.1:4444');


    socket.onopen = function (event) {
        console.log("Соединились");
        var userparams = parseGetParams();
        var userid = {
            code: 2,
            id: userparams.id
        };
        socket.send(JSON.stringify(userid));
    };

    socket.onclose = function (event) {
        console.log("Соединение закрыто");
        var errcode = event.code;
    };

    socket.onerror = function (event) {
        console.log("ОШИБКА");
    };

    socket.onmessage = function (event) {
        var nameholder = document.getElementById("personname");
        nameholder.textContent = event.data;
    };
}

function parseGetParams() {
    var $_GET = {};
    var __GET = window.location.search.substring(1).split("&");
    for(var i=0; i<__GET.length; i++) {
        var getVar = __GET[i].split("=");
        $_GET[getVar[0]] = typeof(getVar[1])=="undefined" ? "" : getVar[1];
    }
    return $_GET;
}