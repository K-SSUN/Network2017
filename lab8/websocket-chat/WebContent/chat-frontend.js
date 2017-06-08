$(function () {
    "use strict";
    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    
    var canvas = document.getElementById('canvas');
    var context = canvas.getContext('2d');
    
    var color = 'black';
    var width = 1;
    var isDown = false;
    var newPoint, oldPoint;

    // my color assigned by the server
    var myColor = false;
    // my name sent to the server
    var myName = false;

    // if user is running mozilla then use it's built-in WebSocket
    window.WebSocket = window.WebSocket || window.MozWebSocket;

    // if browser doesn't support WebSocket, just show some notification and exit
    if (!window.WebSocket) {
        content.html($('<p>', { text: 'Sorry, but your browser doesn\'t ' + 'support WebSockets.'} ));
        input.hide();
        $('span').hide();
        return;
    }

    // open connection
    var connection = new WebSocket('ws://192.168.34.104:8080/websocket-chat/chat');// ip address 수정 필요함   web socket open
    connection.onopen = function () {
        // first we want users to enter their names
        input.removeAttr('disabled');
        status.text('Choose name:');
    };

    connection.onerror = function (error) {
        // just in there were some problems with conenction...
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your ' + 'connection or the server is down.' } ));
    };

    // most important part - incoming messages
    connection.onmessage = function (message) {
        // try to parse JSON message. Because we know that the server always returns
        // JSON this should work without any problem but we should make sure that
        // the massage is not chunked or otherwise damaged.
        try {
            var json = JSON.parse(message.data);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
        // NOTE: if you're not sure about the JSON structure
        // check the server source code above
        if (json.type === 'color') { // first response from the server with user's color   
            myColor = json.data;
            status.text(myName + ': ').css('color', myColor);
            input.removeAttr('disabled').focus();
            // from now user can start sending messages
        } else if (json.type === 'history') { // entire message history		
            // insert every single message to the chat window
            for (var i=0; i < json.data.length; i++) {
                addMessage(json.data[i].author, json.data[i].text,
                           json.data[i].color, new Date(json.data[i].time));
            }
        } else if (json.type === 'message') { // it's a single message				
            input.removeAttr('disabled'); // let the user write another message
            addMessage(json.data.author, json.data.text,
                       json.data.color, new Date(json.data.time));
        } else if (json.type === 'draw') { 
        	if(json.data.color != '0'){
	            context.beginPath();
	            context.lineWidth = json.data.width;
	            context.strokeStyle = json.data.color;
	            context.moveTo(json.data.x1, json.data.y1);
	            context.lineTo(json.data.x2, json.data.y2);
	            context.stroke();
        	} else{
        		context.clearRect(0, 0, canvas.width, canvas.height);
        	}
        } else {
            console.log('Hmm..., I\'ve never seen JSON like this: ', json);
        }
    };

    /**
     * Send mesage when user presses Enter key
     */
    input.keydown(function(e) {
        if (e.keyCode === 13) {
            var msg = $(this).val();
            if (!msg) {
                return;
            }
            // send the message as an ordinary text
            connection.send(JSON.stringify({ type:'message', data: msg}));
            $(this).val('');
            // disable the input field to make the user wait until server
            // sends back response
            input.attr('disabled', 'disabled');

            // we know that the first message sent from a user their name
            if (myName === false) {
                myName = msg;
            }
        }
    });

    /**
     * This method is optional. If the server wasn't able to respond to the
     * in 3 seconds then show some error message to notify the user that
     * something is wrong.
     */
    setInterval(function() {
        if (connection.readyState !== 1) {
            status.text('Error');
            input.attr('disabled', 'disabled').val('Unable to comminucate '
                                                 + 'with the WebSocket server.');
        }
    }, 3000);

    /*  Add message to the chat window	*/
    function addMessage(author, message, color, dt) {
        content.prepend('<p><span style="color:' + color + '">' + author + '</span> @ ' +
             + (dt.getHours() < 10 ? '0' + dt.getHours() : dt.getHours()) + ':'
             + (dt.getMinutes() < 10 ? '0' + dt.getMinutes() : dt.getMinutes())
             + ': ' + message + '</p>');
    }
    
    $(canvas).mousedown(function (event) {
        isDown = true;
        oldPoint = {
            x: event.pageX - $(this).position().left,
            y: event.pageY - $(this).position().top
        };
    });
    $(canvas).mousemove(function (event) {
        if (!isDown) { return; }
        newPoint = {
            x: event.pageX - $(this).position().left,
            y: event.pageY - $(this).position().top
        };
        
        context.beginPath();
        context.lineWidth = width;
        context.strokeStyle = color;
        context.moveTo(oldPoint.x, oldPoint.y);
        context.lineTo(newPoint.x, newPoint.y);
        context.stroke();
        
        var drawElement = {
            x1: oldPoint.x,
            y1: oldPoint.y,
            x2: newPoint.x,
            y2: newPoint.y,
            color: color,
            width: width
        };
        connection.send(JSON.stringify({ type:'draw', data: drawElement}));
        oldPoint = newPoint;
    });
    $(canvas).mouseup(function (event) {
        isDown = false;
        oldPoint = {
            x: event.pageX - $(this).position().left,
            y: event.pageY - $(this).position().top
        };
        
    });
    
    $('#pen').click(function () {
        width = 1;
        color = 'black';
        $('#width').val(width);
    });
    $('#eraser').click(function () {
        width = 10;
        color = 'white';
        $('#width').val(width);
    });
    $('#width').change(function () {
        width = $(this).val()
    });
    // Pen color change
    $('.btn').click(function(){
        var setcolor = $(this).attr('value');
        color = setcolor;
    });
    
    // Clear all canvas
    $('#clearCanvas').click(function(){
        var drawElement = {
                x1: 0,	y1: 0,	x2: 0,	y2: 0,
                color: 0,	width: 0
            };
        connection.send(JSON.stringify({ type:'draw', data: drawElement}));
    });
});