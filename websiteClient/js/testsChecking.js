window.onload = function () {
    //var u = new Irl(window.location);
    var pages_arr = document.getElementsByClassName("menu_btn");
    for(page = 0; page < pages_arr.length; page++) {
        pages_arr[page].href = pages_arr[page].href + window.location.search;
    }
    //console.log(u.id);

    var tests_arr = new Array();
    var scores_arr = new Array();
    var buttons_arr;

    var socket = new WebSocket('ws://127.0.0.1:4444');


    socket.onopen = function (event) {
        console.log("Соединились");
        var userparams = parseGetParams();
        var userid = {
            code: 5,
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
        var reply = JSON.parse(event.data);
        var nameholder = document.getElementById("personname");
        nameholder.textContent = reply.name;
        console.log(event.data)

        var test_list = document.getElementById("tests");
        var unchecked_tests_arr = reply.uncheckedTests;
        for(var i = 0; i < unchecked_tests_arr.length; i++) {
            var test_score = 0;
            var test_li = document.createElement('li');
            tests_arr.push(test_li);
            var a = document.createElement('a');
            a.textContent = unchecked_tests_arr[i].student_name + " - тест по теме \"" + unchecked_tests_arr[i].topic_name
                + "\"";
            a.className = "test_item";
            a.href = "#";
            test_li.appendChild(a);
            test_list.appendChild(test_li);

            //СОДЕРЖАНИЕ ТЕСТА
            var test_data = document.createElement('div');
            test_data.className = "test_data";
            test_data.style.display = 'none';

            //ОТВЕТЫ
            var innerText = "";
            var question_arr = unchecked_tests_arr[i].questions;
            for(var j = 0; j < question_arr.length; j++) {
                var qnum = j + 1;
                innerText += "<b>Вопрос № </b>" + qnum + " " + question_arr[j].question + "<br>";
                for(var ans = 0; ans < question_arr[j].answers.length; ans++) {
                    innerText += ans+1 + ". " + question_arr[j].answers[ans] + "<br>"
                }
                if(question_arr[j].type != 3) {
                    innerText += "Ответ ученика: " + question_arr[j].student_answer + "<br> <b>Набранные баллы: </b>" +
                        question_arr[j].score + "<br>";
                    test_score += question_arr[j].score;
                } else {
                    innerText += "Ответ ученика: " + question_arr[j].student_answer + "<br> <b>Набранные баллы: </b>\"" +
                        "<select class=\"score_selection\"> <option>0</option><option>1</option><option>2</option><option>3</option></select> /3 <br>";
                }
            }
            innerText += "<input class=\"button_check\" type=\"button\" id=\"send\" value=\"Подтвердить проверку\"><br>";
            var score = {
                code: 6,
                topic: unchecked_tests_arr[i].topic,
                student_id: unchecked_tests_arr[i].student_id,
                test_num: unchecked_tests_arr[i].test_num,
                score: test_score
            };
            scores_arr.push(score);
            test_data.innerHTML = innerText;
            test_li.appendChild(test_data);
        }

        console.log(tests_arr);

        //ОБРАБОТКА КЛИКА ПО ТЕСТУ
        for(i = 0; i < tests_arr.length; i++) {
            console.log(tests_arr[i].getElementsByClassName("test_item")[0]);
            tests_arr[i].getElementsByClassName("test_item")[0].onclick = function () {
                openTest(tests_arr, this);
                return false;
            }
        }

        //ОБРАБОТКА КЛИКА ПО КНОПКЕ ПРОВЕРКИ
        buttons_arr = document.getElementsByClassName("button_check");
        for(i = 0; i < buttons_arr.length; i++) {
            buttons_arr[i].onclick = function () {
                socket.send(JSON.stringify(createMessage(buttons_arr, scores_arr, this)));
                return false;
            }
        }
    };
}

function openTest(testarr, openeditem) {
    for(i = 0; i < testarr.length; i++) {
        if(openeditem == testarr[i].getElementsByClassName("test_item")[0]) {
            var test_content = testarr[i].getElementsByClassName("test_data")[0];
            if(test_content.style.display == 'none') {
                test_content.style.display = 'block'
            } else  {
                test_content.style.display = 'none'
            }
        }
    }
}

function createMessage(buttonsarr, scoresarr, clickedbutton) {
    var i = -1;
    for(g = 0; g < buttonsarr.length; g++) {
        if(buttonsarr[g] == clickedbutton)
            i = g;
    }
    var test_content = document.getElementsByClassName("test_data")[i];
    var selections_arr = test_content.getElementsByClassName("score_selection");
    var type_three_score = 0;
    for(t = 0; t < selections_arr.length; t++) {
        type_three_score += +selections_arr[t].options[selections_arr[t].selectedIndex].text;
    }
    scoresarr[i].score += type_three_score;
    test_content.parentElement.style.display = 'none';
    return scoresarr[i];
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