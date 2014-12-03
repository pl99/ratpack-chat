var chat = document.querySelector('#chat');

var ws = new WebSocket('ws://' + location.host + '/ws');
ws.onmessage = function(e) {
    var li = document.createElement('li');
    li.textContent = e.data;
    chat.appendChild(li);
};

var text = document.querySelector('#text');
text.addEventListener('submit', function(e) {
    e.preventDefault();
    var text = e.target.elements['text'];
    var message = text.value;
    if (message) {
        ws.send(message);
    }
    text.value = '';
});