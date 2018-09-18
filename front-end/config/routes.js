var app_controller = require('../app/controllers/app_controller');
var message_controller = require('../app/controllers/message_controller');

module.exports = function (app) {

  app.get('/api/messages', message_controller.load);
  app.post('/api/messages', message_controller.create);
  app.get('/api', app_controller.show);

  // All other routes are handled by Angular
  app.get('/', function (req, res) {
    res.redirect('/#/');
  });


  // catch 404 and forward to error handler
  app.use(function (req, res, next) {
    var err = new Error('Not Found: ' + req.url);
    err.status = 404;
    next(err);
  });

  // error handlers

  // production error handler
  // no stacktraces leaked to user
  app.use(function (err, req, res, next) {
    console.log(err);
    res.status(err.status || 500).send('error', {
      message: err.message,
      error: {}
    });
  });
};
