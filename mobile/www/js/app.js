// App.js
var app = angular.module('app', ['ngCookies', 'ngRoute', 'ngResource', 'ionic', 'app.controllers', 'gridshore.c3js.chart']);

app.run(function ($rootScope, $cookieStore, $state, $ionicPlatform) {
    $ionicPlatform.ready(function() {
        // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
        // for form inputs)
        if (window.cordova && window.cordova.plugins.Keyboard) {
            cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
        }
        if (window.StatusBar) {
            // org.apache.cordova.statusbar required
            StatusBar.styleDefault();
        }
    });
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
            $state.go('dashboard.dash');
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
            })
            .state('dashboard.dash', {
                url: "/dash",
                views: {
                    'menuContent': {
                        templateUrl: "partials/dash.html",
                        controller: 'dash'
                    }
                }
            })
            .state('dashboard.index', {
                url: "/index",
                views: {
                    'menuContent': {
                        templateUrl: "partials/index.html",
                        controller: 'indexCtrl'
                    }
                }
            });
    // default route
    $urlRouterProvider.otherwise("/welcome");

});