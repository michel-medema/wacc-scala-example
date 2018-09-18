var username = 'temp_wacchat';
var password = 'temp_password';

module.exports = {
  db: process.env.MONGOLAB_URI || 'mongodb://' + username + ':' + password + '@ds047484.mongolab.com:47484/heroku_69t1fjj7'
};

