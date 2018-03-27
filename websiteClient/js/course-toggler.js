window.onload= function() {
    document.getElementById('toggler').onclick = function() {
        openclose('active','archive', this);
        return false;
    };
    var active_arr = new Array('subac1','subac2');
    var archive_arr = new Array('subar1','subar2','subar3', 'subar4');
    //console.log(document.getElementById(active_arr[0]));

    var opener_arr = document.getElementsByClassName("submenuopener");
    console.log(Object.keys(opener_arr)[0]);

    /*console.log(document.getElementById("test"));
    console.log(opener_arr[0].getElementsByClassName("submenu")[0]);
    console.log(opener_arr[0].getElementsByClassName("openlink")[0]);*/
    for(i = 0; i < 2; i++) {
        opener_arr[i].getElementsByClassName("openlink")[0].onclick = function () {
            openMenu(active_arr, opener_arr, this);
            return false;
        }
    }

    /*opener_arr[0].onclick = function () {
        openMenu(active_arr,active_arr[0]);
        return false;
    }*/



    /*for (i = 0; i < active_arr.length; i++) {
        document.getElementById(active_arr[i]).onclick = function () {
            openMenu(active_arr,active_arr[i]);
        }
    }*/



};

var arrIndex = function(key, arr){
    var i = 0;
    for(var k in arr){if(k==key){return i;} i++;}
    return false;//если не совпало
}

function openclose(ac,ar, toggler) {
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
}

function openMenu(subarr,openarr, open){
    console.log(open);
    var a = -1;
    for (i = 0; i < 2; i++){
        console.log(openarr[i]);
        if (open != openarr[i].getElementsByClassName("openlink")[0]){
            document.getElementById(subarr[i]).style.display = "none";
        } else {
            a = i;
        }
    }
    if (document.getElementById(subarr[a]).style.display == "block"){
        console.log("Block");
        document.getElementById(subarr[a]).style.display = "none";
    }else{
        console.log("None");
        document.getElementById(subarr[a]).style.display = "block";
    }
}

function test(id) {

    console.log(document.getElementById(id));
    if (document.getElementById(id).style.display == "block"){
        document.getElementById(id).style.display = "none";
    }else{
        document.getElementById(id).style.display = "block";
    }
    return false;
}
