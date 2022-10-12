const $ = document.querySelector.bind(document);
const $$ = document.querySelectorAll.bind(document);

const tabs = $$('.tab-item');
const panes = $$('.tab-pane');

const postTabs = $$('.tab-post-item');
const postPanes = $$('.tab-post-pane');

const tabActive = $('.tab-item.active');
const tabPostActive = $('.tab-post-item.active');

console.log(tabActive, tabPostActive)

const line = $('.tabs .line');

line.style.left = tabActive.offsetLeft + "px"
line.style.width = tabActive.offsetWidth + "px"

tabs.forEach((tab, index) => {
    const pane = panes[index]
    tab.onclick = function(){

        $('.tab-item.active').classList.remove('active')
        $('.tab-pane.active').classList.remove('active')

        line.style.left = this.offsetLeft + "px"
        line.style.width = this.offsetWidth + "px"

        this.classList.add('active')
        pane.classList.add('active')
    }
})
postTabs.forEach((postTab, index) => {
    const postPane = postPanes[index]
    postTab.onclick = function(){

        $('.tab-post-item.active').classList.remove('active')
        $('.tab-post-pane.active').classList.remove('active')

        this.classList.add('active')
        postPane.classList.add('active')
    }
})
var userSettings = document.querySelector(".user-settings");

function UserSettingToggle(){
    userSettings.classList.toggle("user-setting-showup-toggle");
}