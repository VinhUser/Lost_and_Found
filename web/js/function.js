var comment_hidden = document.getElementById("test-d");
 var report_hidden = document.getElementById("test-r");
function comment_dl(){

    report_hidden.style.display="none";
    if(    comment_hidden.style.display=="none"){
        comment_hidden.style.display="block";
    }
    else{
        comment_hidden.style.display="none";
    }
}
function report_dl(){
    comment_hidden.style.display="none";
    if(    report_hidden.style.display=="none"){
        report_hidden.style.display="block";
    }
    else{
        report_hidden.style.display="none";
    }
}
var userSettings = document.querySelector(".user-settings");

function UserSettingToggle(){
    userSettings.classList.toggle("user-setting-showup-toggle");
}
