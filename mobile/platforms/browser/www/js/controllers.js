(function () {
    'use strict';

    angular.module('app.controllers', [])
        /*
         .controller('AppCtrl', function($scope, $ionicModal, $timeout) {

         // With the new view caching in Ionic, Controllers are only called
         // when they are recreated or on app start, instead of every page change.
         // To listen for when this page is active (for example, to refresh data),
         // listen for the $ionicView.enter event:
         //$scope.$on('$ionicView.enter', function(e) {
         //});

         // Create the login modal that we will use later
         $ionicModal.fromTemplateUrl('templates/login.html', {
         scope: $scope
         }).then(function(modal) {
         $scope.modal = modal;
         });

         // Triggered in the login modal to close it
         $scope.closeLogin = function() {
         $scope.modal.hide();
         };

         // Open the login modal
         $scope.login = function() {
         $scope.modal.show();
         };
         })

         .controller('PlaylistsCtrl', function($scope) {
         $scope.playlists = [
         { title: 'Reggae', id: 1 },
         { title: 'Chill', id: 2 },
         { title: 'Dubstep', id: 3 },
         { title: 'Indie', id: 4 },
         { title: 'Rap', id: 5 },
         { title: 'Cowbell', id: 6 }
         ];
         })

         .controller('PlaylistCtrl', function($scope, $stateParams) {
         })*/

        .controller('Login', ['$scope', '$stateParams', function ($scope, $stateParams) {
            var vm = this;

            // Form data for the login modal
            vm.loginData = {};
            vm.msgError = '';

            vm.login = {"username": "joao", "password": "123"};

            // Perform the login action when the user submits the login form
            vm.doLogin = function () {
                if ((vm.loginData.password == vm.login.password) && (vm.loginData.username == vm.login.username)) {
                    vm.msgError = '';
                    console.log("igual >>>> passed");
                } else {
                    vm.msgError = "Login ou senha incorreta";
                    console.log("diferente <<<< don't passed");
                }
            };
        }]);
})();