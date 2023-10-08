const ws = new WebSocket(
    "ws://localhost:8090/ws/events/", ["protocolOne"]
);

ws.addEventListener('open', (connection) => {
    console.log('connection opened', connection)
    ws.send("Ok, let kick off this thing!");
    const blob = new Blob(['the client is now connected'], {type: 'text/plain'});
    ws.send(blob);
});

ws.addEventListener('error', (cause) => {
    console.log('connection encountered an error', cause);
});

ws.addEventListener('message', (message) => {
    console.log('received message from server', message.data);
});

ws.addEventListener('close', (status, reason) => {
    console.log('server closed connection', status, reason);
});

function sendMessage() {
    // Construct a msg object containing the data the server needs to process the message from the chat client.
    const msg = {
        type: "message",
        text: document.getElementById("message").value,
        id: 'testing',
        date: Date.now(),
    };

    // Send the msg object as a JSON-formatted string.
    ws.send(JSON.stringify(msg));

    // Blank the text input element, ready to receive the next line of text from the user.
    document.getElementById("message").value = "";
}

function closeConnection() {
    ws.close(3000, 'client has disconnected')
}