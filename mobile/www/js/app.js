// App.js
var app = angular.module('app', ['ngCookies', 'ionic', 'app.controllers']);

app.run(function ($rootScope, $cookieStore, $state) {
    // Check login session
    $rootScope.$on('$stateChangeStart', function (event, next, current) {
        var userInfo = $cookieStore.get('userInfo');
        if (!userInfo) {
            // user not logged in | redirect to login
            if (next.name !== "welcome") {
                // not going to #welcome, we should redirect now
                event.preventDefault();
                $state.go('welcome');
            }
        } else if (next.name === "welcome") {
            event.preventDefault();
            $state.go('dashboard');
        }
    });
});

// Routes
app.config(function ($stateProvider, $urlRouterProvider) {
    // setup states
    $stateProvider
            .state('welcome', {
                url: "/welcome",
                templateUrl: "partials/welcome.html",
                controller: 'welcomeCtrl'
            })
            .state('dashboard', {
                url: "/dashboard",
                templateUrl: "partials/dashboard.html",
                controller: "dashboardCtrl"
            })
            .state('newcar', {
                url: "/newcar",
                templateUrl: "partials/newCar.html",
                controller: "dashboardCtrl"
            });
    // default route           
    $urlRouterProvider.otherwise("/welcome");

});