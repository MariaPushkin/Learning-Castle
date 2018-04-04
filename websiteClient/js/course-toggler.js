window.onload= function() {
    /*document.getElementById('toggler').onclick = function() {
        openclose('active','archive', this);
        return false;
    };*/
    var mainpage = document.getElementById("mainpage");
    var courcespage = document.getElementById("courcespage");
    mainpage.href = "teacherMain.html" + window.location.search;
    courcespage.href ="teacherCources.html" + window.location.search;
    console.log(courcespage.href);

    var archive_arr = new Array();
    var opener_arr = new Array();
    var resultlink_arr = new Array();

    var socket = new WebSocket('ws://127.0.0.1:4444');

    socket.onopen = function (event) {
        console.log("Соединились");
        var userparams = parseGetParams();
        var userid = {
            code: 3,
            id: userparams.id
        };
        socket.send(JSON.stringify(userid));
    };

    socket.onmessage = function (event) {
        if(typeof event.data == 'string') {
            console.log("Ответ от сервера " + event.data);
            var reply = JSON.parse(event.data);
            if (reply.code = 1) {
                var nameholder = document.getElementById("personname");
                nameholder.textContent = reply.teacherName;
                var blankmenu_arr = document.getElementsByClassName("mainmenu");
                var years_arr = new Array();
                for (var i = 0, ln = reply.courses.length; i < ln; i++) {
                    if (reply.courses[i].startYear == '2017-09-01') {
                        var li = document.createElement('li');
                        li.style.marginLeft = '10px';
                        var a = document.createElement('a');
                        a.href = "";
                        a.innerHTML = reply.courses[i].name;
                        a.id = reply.courses[i].id;
                        a.className = "openresult";
                        resultlink_arr.push(a);
                        li.appendChild(a);
                        blankmenu_arr[0].appendChild(li);
                    } else {
                        var index = years_arr.indexOf(reply.courses[i].startYear);
                        if (index != -1) {
                            var submenu_ul = document.getElementById("subar" + index);
                            var a2 = document.createElement('a');
                            li2.style.marginLeft = '10px';
                            var a2 = document.createElement('a');
                            a2.href = "";
                            a2.innerHTML = reply.courses[i].name;
                            a2.id = reply.courses[i].id;
                            a2.className = "openresult";
                            resultlink_arr.push(a2);
                            li2.appendChild(a2);
                            ul.appendChild(li2);
                        } else {
                            years_arr.push(reply.courses[i].startYear);
                            var startyear = reply.courses[i].startYear.substring(0, 4);
                            var endyear = +startyear + 1;
                            var li = document.createElement('li');
                            li.style.marginLeft = '10px';
                            li.className = "submenuopener";
                            opener_arr.push(li);
                            var a = document.createElement('a');
                            a.href = "";
                            a.innerHTML = startyear + "-" + endyear;
                            a.className = "openlink";
                            li.appendChild(a);

                            var ul = document.createElement('ul');
                            li.appendChild(ul);
                            ul.id = "subar" + opener_arr.indexOf(li);
                            archive_arr.push("subar" + opener_arr.indexOf(li));
                            ul.className = "submenu";
                            var li2 = document.createElement('li');
                            li2.style.marginLeft = '10px';
                            var a2 = document.createElement('a');
                            a2.href = "";
                            a2.innerHTML = reply.courses[i].name;
                            a2.id = reply.courses[i].id;
                            a2.className = "openresult";
                            resultlink_arr.push(a2);
                            li2.appendChild(a2);
                            ul.appendChild(li2);

                            blankmenu_arr[1].appendChild(li);
                        }
                    }
                }
            }
        }
        console.log(years_arr);
        console.log(opener_arr);
        for(i = 0; i < opener_arr.length; i++) {
            opener_arr[i].getElementsByClassName("openlink")[0].onclick = function () {
                openMenu(archive_arr, opener_arr, this);
                return false;
            }
        }

        console.log(resultlink_arr);
        for(i = 0; i < resultlink_arr.length; i++) {
            document.getElementById(resultlink_arr[i].id).onclick = function () {
                var userparams = parseGetParams();
                var userid = {
                    code: 4,
                    teacher_id: userparams.id,
                    course_id: this.id
                };
                socket.send(JSON.stringify(userid));
                return false;
            }
        }
    };
};

var arrIndex = function(key, arr){
    var i = 0;
    for(var k in arr){if(k==key){return i;} i++;}
    return false;//если не совпало
}



/*function openclose(ac,ar, toggler) {
    var act = document.getElementById(ac);
    var arh = document.getElementById(ar);
    if(act.style.display == 'block') {
        act.style.display = 'none';
        arh.style.display = 'block';
        toggler.innerHTML = 'Активные';
    } else {
        act.style.display = 'block';
        arh.style.display = 'none';
        toggler.innerHTML = 'Архив';
    }
}*/

function openMenu(subarr,openarr, open){
    var a = -1;
    for (i = 0; i < openarr.length; i++){
        console.log(openarr[i]);
        if (open != openarr[i].getElementsByClassName("openlink")[0]){
            document.getElementById(subarr[i]).style.display = "none";
        } else {
            a = i;
        }
    }
    console.log(a);
    if (document.getElementById(subarr[a]).style.display == "block"){
        console.log("Block");
        document.getElementById(subarr[a]).style.display = "none";
    }else{
        console.log("None");
        document.getElementById(subarr[a]).style.display = "block";
    }
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


