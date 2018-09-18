require('./config/nodetime');
var fs = require('fs');
var express = require('express');
var path = require('path');
var app = express();

// Require our config variables
var config = require('./config/config');

// Require all express boilerplate
require('./config/express')(app);

app.use('/bower_components', express.static(__dirname + '/bower_components'));
app.use(express.static(path.join(__dirname, 'public')));

// Load all models
fs.readdirSync(__dirname + '/app/models').forEach(function (file) {
  if (~file.indexOf('.js')) require(__dirname + '/app/models/' + file);
});

// Initialize MongoDB
require('./config/mongo')(app, config);

// API routes
require('./config/routes')(app);

// Websocket support
require('./config/websocket')(app);

module.exports = app;
