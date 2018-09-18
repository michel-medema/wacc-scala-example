var http = require('http');
var message_socket_controller = require('../app/controllers/message_socket_controller');

module.exports = function(app) {
  var server = http.createServer(app);

  //var io = require('socket.io').listen(server);
  //io.sockets.on('connection', message_socket_controller.manage_messages);

  app.set('port', process.env.PORT || 8000);

  server.listen(app.get('port'), function() {
    console.log('Express server listening on port ' + app.get('port'));
  });
};
