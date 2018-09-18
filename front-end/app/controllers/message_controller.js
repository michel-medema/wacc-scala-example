var mongoose = require('mongoose');
var Message = mongoose.model('Message');

exports.load = function (req, res) {
  Message.find(function (err, messages) {
    if (err) return console.error(err);
    res.send(messages)
  });
};

exports.show = function (req, res, id) {
  Message.find({ id: id }, function (err, message) {
    res.send(message)
  });
};

exports.create = function (req, res) {
  var message = new Message(req.body);
  message.save(function (err, message) {
    if (err) return console.error(err);
    res.status(201).end();
  });
};