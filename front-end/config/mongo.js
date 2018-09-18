var mongoose = require('mongoose');

module.exports = function (app, config) {
  mongoose.connect(config.db);

  // Make our db accessible to our router
  app.use(function (req, res, next) {
    req.db = mongoose.connection;
    next();
  });
};