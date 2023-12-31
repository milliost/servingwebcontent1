'use strict';

var messageArea = document.querySelector('#messageArea');
var stompClient = null;
var username = null;


connect()
function connect() {
    let request = new XMLHttpRequest();
    let url = "http://localhost:8080/myName";
    request.open("GET", url);
    request.responseType = "text";
    request.send();
    request.onload = function () {
        username = request.response;
        if (username) {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected);
        }
    }

}


function onConnected() {
    stompClient.subscribe('/lobbies', onMessageReceived);
    stompClient.send("/app/chat.addUser",
        {},
        JSON.stringify({sender: username, type: 'JOIN'})
    )
    setTimeout(() => {getHistory()}, 250);
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + message.content;

    }else if (message.type === 'LEAVE') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' left!';

    }else if(message.type === 'HISTORY'){
        var conT = message.content;
        let array = conT
            .replaceAll(',','')
            .replace('[','')
            .replace(']','')
            .split(" ");
        for(let i=0; i<array.length;i++){
            if(array[i]==="null") array[i]="свободно"
            tableName(i,array[i]);
        }
        messageElement.classList.add('event-message');


    } else {
        messageElement.classList.add('chat-message');
        var avatarElement = document.createElement('i');
        var avatarText = document.createTextNode(message.sender[0]);
        avatarElement.appendChild(avatarText);
        avatarElement.style['background-color'] = getAvatarColor(message.sender);
        messageElement.appendChild(avatarElement);
        var usernameElement = document.createElement('span');
        var usernameText = document.createTextNode(message.sender);
        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
    }
    var textElement = document.createElement('p');
    var messageText = document.createTextNode(message.content);
    textElement.appendChild(messageText);
    messageElement.appendChild(textElement);
    messageArea.appendChild(messageElement);
    messageArea.scrollTop = messageArea.scrollHeight;
}
function getHistory() {

    let chatMessage = {
        sender: username,
        content: "",
        type: 'HISTORY'
    };
    stompClient.send("/app/chat.history", {}, JSON.stringify(chatMessage));
}
