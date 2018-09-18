wacchatApp.controller('ChatController', function($scope, $http, $timeout, Message, socket) {
  $scope.message = {};
  $http({
    method: 'GET',
    url: 'http://129.125.75.187:8080/api'
  }).success(function(data) {
    $scope.chat_content = data;
  });

  $scope.send_message = function() {
    if ($scope.message.name === undefined || $scope.message.content === undefined) {
      // Some jQuery (can also be done with angular)
      var alert_div = $("#alert");
      var message = alert_div.find("#message");
      message.text("Please provide a name and a message!");
      alert_div.show();
      return false;
    }
    Message.save($scope.message);
    console.log("Sending message over websocket");
    socket.emit('send_message', $scope.message);
    add_message($scope.message);
    $scope.message = {
      name: $scope.message.name
    };
  };

  socket.on('receive_message', function(data) {
    console.log("Receievd socket data " + data);
    add_message(data);
  });

  function add_message(message) {
    $scope.messages = $scope.messages || [];
    $scope.messages.push(message);
  }

  $scope.refresh = function() {
    Message.query(function(data) {
      $scope.messages = data;
      console.log('Refreshed: ' + data);
    });
  };
});
