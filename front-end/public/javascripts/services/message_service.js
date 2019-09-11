wacchatServices.factory('Message', function ($resource) {
  return $resource('http://129.125.235.8:8080/api/messages/:messageId', {messageId: '@messageId'});
});
