angular
    .module('app', ['ngRoute', 'ngResource', 'lbServices', 'ui.router'])
    .config(function($routeProvider) {
        $routeProvider
            .when('/', {
                redirectTo: '/api/main'
            })
            .when('/api/main', {
                templateUrl: 'views/main.html',
                controller: 'Main',
                controllerAs: 'main'
            })
            .when('/api/todo', {
                templateUrl: 'views/todo.html',
                controller: 'TodoController'
            })
            .otherwise({redirectTo: '/'});
    });
