window.onload = function () {
    var label = document.getElementById("status");
    var login = document.getElementById("login");
    var password = document.getElementById("password");
    var btnLogin = document.getElementById("send");

    btnLogin.onclick = function () {
        //проверка, что соединение открыто
        if(socket.readyState == WebSocket.OPEN) {
            var user = {
                login: login.value,
                password: password.value
            };
            console.log(user);
            socket.send(JSON.stringify(user));
        }
    };
    /*btnStop.onclick = function () {
        //проверка, что соединение открыто
        if(socket.readyState == WebSocket.OPEN) {
            socket.close();
        }
    };*/
    var socket = new WebSocket('ws://127.0.0.1:4444');

    socket.onopen = function (event) {
        console.log("Соединились");
        label.innerHTML = 'Соединились';
    };

    socket.onclose = function (event) {
        console.log("Соединение закрыто");
        label.innerHTML = 'Соединение закрыто';
        //код ошибки
        var errcode = event.code;
        if(event.wasClean) {
            label.innerHTML = 'Соединение закрыто корректно';
        } else {
            //причина ошибки
            label.innerHTML = 'Соединение закрыто с ошибкой' + event.reason;
        }
    };

    socket.onerror = function (event) {
        console.log("ОШИБКА");
    };

    socket.onmessage = function (event) {
        if(typeof event.data == 'string') {
            console.log("Ответ от сервера " + event.data);
            var reply = JSON.parse(event.data);
            console.log(reply);
            if(reply.status == "OK") {
                if(reply.rights == "student") {
                    window.location.href = "studentMain.html";
                } else if(reply.rights == "teacher") {
                    window.location.href = "teacherMain.html";
                }
            }
        }
    };
};
/*
* 1. Проверка поддержки браузерами (все последнии поддерживают), но можно if window.websocket итп
* 2. Создание объекта
* 3. Соединение с сервером
* 4. Назначение обработчиков событий
* 5. Обмен информации с сервером
* 6. Закрытие соединения*/