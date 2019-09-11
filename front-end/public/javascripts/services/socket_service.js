wacchatApp.factory('socket', function ($rootScope) {
/*
  //return null;
  var socket = io.connect('http://129.125.75.187:8080', {
    transports: ['websocket']
  });

  //// Copied from http://www.ibm.com/developerworks/library/wa-nodejs-polling-app/
  return {
    on: function (eventName, callback) {
      socket.on(eventName, function () {
	console.log("Socket on");
        var args = arguments;
        $rootScope.$apply(function () {
          callback.apply(socket, args);
        });
      });
    },
    emit: function (eventName, data, callback) {
      socket.emit(eventName, data, function () {
        var args = arguments;
	console.log("Socket emit");
        $rootScope.$apply(function () {
          if (callback) {
            callback.apply(socket, args);
          }
        });
      });
    }
  };
*/
  var socket =  new WebSocket("ws://129.125.235.8:8080/socket.io/");
  
  return {
    on: function (eventName, callback) {
      socket.onmessage = function (event) {
        console.log(event.data);
	var args = arguments;
	console.log(arguments);
	//callback(JSON.parse(event.data));
	$rootScope.$apply(function () {
	  callback(JSON.parse(event.data));
	});
      }
    },
    emit: function (eventName, data, callback) {
      console.log("Sending " + data);
      socket.send(JSON.stringify(data));
      var args = arguments;
      //$rootScope.$apply(function () {
	//callback.apply(socket, args);
      //});
    }
  }

});
