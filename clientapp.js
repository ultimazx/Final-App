var serialport = require('serialport'),
	
SerialPort = serialport.SerialPort,
	
io = require('socket.io-client');


socket = io.connect('http://192.168.2.4:8080');


var sp = new SerialPort('/dev/ttyUSB0',{
	
baudrate: 9600,
	
parser: serialport.parsers.readline('\r\n')
});



var codecheck;
var text;
sp.open(function(){
        console.log('Serial port connected');
});

sp.on('data', function(data){
        if(data =='a')
		if((data != '1') || (data != '2') || (data != '3') || (data != '6280'))
			text = data;
		else
			handshake(data);
        else
		if(data == '4')
                	READ();
		else
			if(data == '5')
				WRITER();
			else
				sendData('Error');
	
});

socket.on('disconnect', function(){
        sp.write('0');
});

function handshake(data){
        sp.write('a');
	LED(data);
}

function sendData(data){
        socket.emit('temp', data);
}

function LED(data){
                sp.write(data);
}

var fs  = require('fs');

var lineReader = require('line-reader');

function READ(){
lineReader.eachLine('file.txt', function(line, last) {
  console.log(line);
  // do whatever you want with line...
	sendData(line);
  if(last){
    // or check if it's the last one
	sendData(line);
  }
});
}
function WRITER(){
	fs.writeFile('/blank.txt', text, function(err){
		if(err){
			console.log(err);
		}else{
			console.log('The file was saved!');
		}
	});
}






