var wacchatApp = angular.module('wacchat', ['ngRoute',
                                              'wacchatControllers',
                                              'wacchatServices']);

var wacchatControllers = angular.module('wacchatControllers', []);
var wacchatServices = angular.module('wacchatServices', ['ngResource']);

wacchatApp.config(['$routeProvider',
  function($routeProvider) {
  $routeProvider
    .otherwise({
      redirectTo:'/',
      controller:'ChatController',
      templateUrl:'views/chat.html'
    });
}]);
