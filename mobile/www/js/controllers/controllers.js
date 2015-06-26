var app = angular.module('app.controllers', []);
app.factory('dadosUser', function(){
    var id = 1,
        veiculos = [{id: id, placa: '132456', renavan: '4654asfaflh'}];

    return {
        getVeiculos: getVeiculos,
        setVeiculo: setVeiculo
    };

    function getVeiculos() {
        return veiculos
    }

    function setVeiculo(car) {
        car.id = id + 1;
        veiculos.push(car)
    }
})

    .controller('welcomeCtrl', function ($scope, $state, $cookieStore, $location) {

    /**
     * SOCIAL LOGIN
     * Facebook and Google
     */
    // FB Login
    $scope.fbLogin = function () {
        FB.login(function (response) {
            if (response.authResponse) {
                getUserInfo();
            } else {
                console.log('User cancelled login or did not fully authorize.');
            }
        }, {scope: 'email,user_photos,user_videos'});

        function getUserInfo() {
            // get basic info
            FB.api('/me', function (response) {
                console.log('Facebook Login RESPONSE: ' + angular.toJson(response));
                // get profile picture
                FB.api('/me/picture?type=normal', function (picResponse) {
                    console.log('Facebook Login RESPONSE: ' + picResponse.data.url);
                    response.imageUrl = picResponse.data.url;
                    // store data to DB - Call to API
                    // Todo
                    // After posting user data to server successfully store user data locally
                    var user = {};
                    user.name = response.name;
                    user.email = response.email;
                    if(response.gender) {
                        response.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                    } else {
                        user.gender = '';
                    }
                    user.profilePic = picResponse.data.url;
                    $cookieStore.put('userInfo', user);
                    $location.path('/dashboard/index');

                });
            });
        }
    };
    // END FB Login

    // Google Plus Login
    $scope.gplusLogin = function () {
        var myParams = {
            // Replace client id with yours
            'clientid': '18301237550-3vlqoed2en4lvq6uuhh88o2h1l9m70tr.apps.googleusercontent.com',
            'cookiepolicy': 'single_host_origin',
            'callback': loginCallback,
            'approvalprompt': 'force',
            'scope': 'https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.profile.emails.read'
        };
        gapi.auth.signIn(myParams);

        function loginCallback(result) {
            if (result['status']['signed_in']) {
                var request = gapi.client.plus.people.get({'userId': 'me'});
                request.execute(function (resp) {
                    console.log('Google+ Login RESPONSE: ' + angular.toJson(resp));
                    var userEmail;
                    if (resp['emails']) {
                        for (var i = 0; i < resp['emails'].length; i++) {
                            if (resp['emails'][i]['type'] == 'account') {
                                userEmail = resp['emails'][i]['value'];
                            }
                        }
                    }
                    // store data to DB
                    var user = {};
                    user.name = resp.displayName;
                    user.email = userEmail;
                    if(resp.gender) {
                        resp.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                    } else {
                        user.gender = '';
                    }
                    user.profilePic = resp.image.url;
                    $cookieStore.put('userInfo', user);
                    $location.path('/dashboard/index');
                });
            }
        }
    };
    // END Google Plus Login

});

// Dashboard/Profile Controller
app.controller('dashboardCtrl', [
    '$scope', 'dadosUser', '$resource', '$window', '$state', '$cookieStore', '$ionicModal', '$timeout', '$ionicSideMenuDelegate',
    function ($scope, dadosUser, $resource, $window, $state, $cookieStore, $ionicModal, $timeout, $ionicSideMenuDelegate) {
    // Set user details
    $scope.user = $cookieStore.get('userInfo');
    
    // Logout user
    $scope.logout = function () {
        $cookieStore.remove("userInfo");
        $state.go('welcome');
        $window.location.reload();
    };

    // Form data for the carro modal
    $scope.car = {};

    // Create the carro modal that we will use later
    $ionicModal.fromTemplateUrl('partials/newCar.html', {
        scope: $scope
    }).then(function(modal) {
        $scope.modal = modal;
    });

    // Triggered in the carro modal to close it
    $scope.closeAddCar = function() {
        $scope.modal.hide();
    };

    // Open the carro modal
    $scope.newCar = function() {
        $scope.modal.show();
    };

    $scope.openmenu = function() {
        $ionicSideMenuDelegate.toggleLeft();
    };

    // Perform the carro action when the user submits the carro form
    $scope.doAdd = function() {
        //$scope.car.email = $scope.user.email;
        $scope.msg = 'Veículo adicionado com sucesso';
        dadosUser.setVeiculo($scope.car);
        $scope.car = {};

        console.log(dadosUser.getVeiculos());
/*
        var newCarPromise = $resource('/').save({}, $scope.car).$promise;

        newCarPromise
            .then(function (data) {
                $scope.msg = 'Veículo adicionado com sucesso';
                $scope.carros.append($scope.car);
            })
            .catch(function(error) {
                console.log(error)
            });*/

        // Simulate a carro delay. Remove this and replace with your carro
        // code if using a carro system
        $timeout(function() {
            $scope.closeAddCar();
        }, 500);
    };
}])
    .controller('indexCtrl', ['$scope', 'dadosUser', function($scope, dadosUser) {
        $scope.carros = dadosUser.getVeiculos()
    }]);