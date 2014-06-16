//create server and socket connection between app and client
var app = require('http').createServer(),
        io = require('socket.io').listen(app);

//listen to the 8080 channel
app.listen(8080,'192.168.2.4');


io.sockets.on('connection', function (socket) {
	//write data to the app
        socket.on('temp', function(data){
		console.log(data);
                socket.emit('data', data);
		
        });
});
