(function () {
    'use strict';
    angular.module('app', ['ionic', 'ngRoute', 'ngResource', 'ngCookies', 'app.controllers'])

        .run(function ($rootScope, $cookieStore, $state) {
            // Check login session
            $rootScope.$on('$stateChangeStart', function (event, next, current) {
                var userInfo = $cookieStore.get('userInfo');
                if (!userInfo) {
                    // user not logged in | redirect to login
                    if (next.name !== "welcome") {
                        // not going to #welcome, we should redirect now
                        event.preventDefault();
                        $state.go('login');
                    }
                } else if (next.name === "welcome") {
                    event.preventDefault();
                    $state.go('dashboard');
                }
            });
        })

        .config(['$routeProvider', function ($routeProvider) {
            $routeProvider.when('/', {
                redirectTo: '/login'
            })
            .when('/login', {
                templateUrl: 'templates/login.html',
                controller: 'LoginSociais'
            })
            .otherwise({
                redirectTo: '/'
            });
        }]);
})();