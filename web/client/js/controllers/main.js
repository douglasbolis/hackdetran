angular
    .module('app')
    .controller('Main', ['$scope', '$state', 'UserProfile', function($scope, $state, user) {

        var vm = this;

        vm.users = [];
        function getUsers() {
            user
                .find()
                .$promise
                .then(function(results) {
                    vm.users = results;
                    console.log(vm.users);
                });
        }
        getUsers();

        vm.addUser = function() {
            user
                .create(vm.newUser)
                .$promise
                .then(function(user) {
                    vm.newUser = '';
                    $('.focus').focus();
                    getUsers();
                });
        };

        vm.removeUser = function(item) {
            user
                .deleteById(item)
                .$promise
                .then(function() {
                    getUsers();
                });
        };
    }]);
