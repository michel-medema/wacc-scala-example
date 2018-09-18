var mongoose = require('mongoose');

var MessageSchema = mongoose.Schema({
  name: String,
  content: String
});

mongoose.model('Message', MessageSchema);