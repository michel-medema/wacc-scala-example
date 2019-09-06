wacchatServices.factory('Message', function ($resource) {
  return $resource('http://localhost:8080/api/messages/:messageId', {messageId: '@messageId'});
});
