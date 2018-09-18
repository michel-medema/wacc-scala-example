wacchatServices.factory('Message', function ($resource) {
  return $resource('http://129.125.75.187:8080/api/messages/:messageId', {messageId: '@messageId'});
});
