const url = "ws://localhost:8888/ws/events"
let webSocket = new WebSocket(url, "protocolOne");
let clientID = "";

webSocket.onopen = (event) => {
    webSocket.send("Here's some text that the server is urgently awaiting!");
};

function setUsername() {
    console.log("currently doing nothing")
}

webSocket.onmessage = (event) => {
    const content = document.getElementById("chat-messages").contentDocument;
    let text = "";
    const msg = JSON.parse(event.data);
    const time = new Date(msg.date);
    const timeStr = time.toLocaleTimeString();

    switch (msg.type) {
        case "id":
            clientID = msg.id;
            setUsername();
            break;
        case "username":
            text = `User <em>${msg.name}</em> signed in at ${timeStr}<br>`;
            break;
        case "message":
            text = `(${timeStr}) ${msg.name} : ${msg.text} <br>`;
            break;
        case "rejectusername":
            text = `Your username has been set to <em>${msg.name}</em> because the name you chose is in use.<br>`;
            break;
        case "userlist":
            document.getElementById("userlistbox").innerHTML = msg.users.join("<br>");
            break;
    }

    if (text.length) {
        content.write(text);
        document.getElementById("chat-messages").contentWindow.scrollByPages(1);
    }
};

// Send text to all users through the server
function sendText() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    const msg = {
        type: "message",
        text: document.getElementById("message").value,
        id: clientID,
        date: Date.now(),
    };

    // Send the msg object as a JSON-formatted string.
    webSocket.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    document.getElementById("message").value = "";
}

function closeConn(){
    webSocket.close();
}