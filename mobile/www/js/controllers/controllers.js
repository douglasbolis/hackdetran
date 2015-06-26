(function() {
    'use strict';

    var app = angular.module('app.controllers', []);

    app
        .factory('pathFactory', function($resource) {
            return $resource('www.alguma...')
        })

        .factory('reqUser', ['$scope', 'pathFactory', function($scope, pathFactory) {
            var dadosUser = [],
                newCarPromise = pathFactory.save({}, $scope.car).$promise;

            return {
                getDadosUsuario: getDadosUsuario
            };

            newCarPromise
                .then(function (data) {
                    dadosUser = data;
                })
                .catch(function(error) {
                    console.log(error)
                });

            function getDadosUsuario() {
                return dadosUser
            }
        }])

        .factory('DadosVeiculos', ['pathFactory', function (pathFactory) {
            var veiculos = [{placa: '132456', renavan: '4654asfaflh'}],
                dados = [
                    {
                        "label": "Placa",
                        "value": "OCZ8775"
                    }, {
                        "label": "Renavam",
                        "value": "00345508807"
                    }, {
                        "label": "Placa Anterior",
                        "value": "OCZ8775/ES"
                    }, {
                        "label": "Tipo",
                        "value": "6-AUTOMOVEL"
                    }, {
                        "label": "Categoria",
                        "value": "1-Particular"
                    }, {
                        "label": "Espécie",
                        "value": "1-Passageiro"
                    }, {
                        "label": "Lugares",
                        "value": "5"
                    }, {
                        "label": "Marca/Modelo",
                        "value": "159923-FORD/FIESTA FLEX         (Nacional)"
                    }, {
                        "label": "Fabricação/Modelo",
                        "value": "2011/2012"
                    }, {
                        "label": "Potência",
                        "value": "73"
                    }, {
                        "label": "Combustível",
                        "value": "16-Alcool-Gasol"
                    }, {
                        "label": "Cor",
                        "value": "11-PRETA"
                    }, {
                        "label": "Carroceria",
                        "value": "999-NAO APLICAVEL"
                    }, {
                        "label": "Nome do Proprietário",
                        "value": "CLAYTON SANTOS DA SILVA"
                    }, {
                        "label": "Recadastrado DETRAN",
                        "value": "DetranNet"
                    }, {
                        "label": "Proprietário Anterior",
                        "value": "CONTAUTO CONTINENTE AUTOMOVEIS LTDA"
                    }, {
                        "label": "Origem dos Dados do Veículo",
                        "value": "CADASTRO"
                    }, {
                        "label": "Município de Emplacamento",
                        "value": "VITORIA"
                    }, {
                        "label": "Licenciado até",
                        "value": "2011 em 05/09/2011 através do Registro de Veículo (CRV)(Via 1)"
                    }, {
                        "label": "Adquirido em",
                        "value": "30/08/2011"
                    }, {
                        "label": "Situação",
                        "value": "Em Circulação"
                    }, {
                        "label": "Restrição à Venda",
                        "value": "Alienação Fiduciária em favor de BANCO ITAUCARD SA"
                    }, {
                        "label": "Informações do Contrato e/ou Aditivo",
                        "value": "Sem dados do contrato e/ou aditivo"
                    }, {
                        "label": "Informações PENDENTES originadas das financeiras via SNG - Sistema Nacional de Gravame",
                        "value": "Registro de Baixa de Alienação Fiduciária informado por BANCO ITAUCARD SA                        em 19/03/2015 às 16h32min para CLAYTON SANTOS DA SILVA"
                    }, {
                        "label": "Indicativo de Clonagem (informação de responsabilidade do proprietário)",
                        "value": "Não"
                    }, {
                        "label": "Impedimentos",
                        "value": "RENAJUD"
                    }, {
                        "label": "Averbação judicial",
                        "value": "Não"
                    }];

            return {
                getVeiculos: getVeiculos,
                setVeiculo: setVeiculo,
                getDados: getDados
            };

            function getDados() {
                return dados
            }

            function getVeiculos() {
                var getVeiculosPromise = pathFactory.query({
                    "method": "GET",
                    "headers": {
                        "X-AUTH-TOKEN": "1234"
                    }
                }).$promise;

                return veiculos
            }

            function setVeiculo(car) {
                veiculos.push(car)
            }
        }])

        .controller('welcomeCtrl', ['$scope', 'reqUser', '$state', '$cookieStore', '$location',
            function ($scope, reqUser, $state, $cookieStore, $location) {

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
                    user.profilePic = picResponse.data.url;
                    $cookieStore.put('userInfo', user);
                    $location.path('/dashboard/dash');
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
                            if (response.gender) {
                                response.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                            } else {
                                user.gender = '';
                            }
                            user.profilePic = picResponse.data.url;
                            $cookieStore.put('userInfo', user);
                            reqUser.getDadosUsuario();
                            $location.path('/dashboard/veiculos');
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
                            if (resp.gender) {
                                resp.gender.toString().toLowerCase() === 'male' ? user.gender = 'M' : user.gender = 'F';
                            } else {
                                user.gender = '';
                            }
                            user.profilePic = resp.image.url;
                            $cookieStore.put('userInfo', user);
                            $location.path('/dashboard/veiculos');
                        });
                    }
                }
            };
            // END Google Plus Login

        }]);

// Dashboard/Profile Controller
    app.controller('dashboardCtrl', [
        '$scope', 'pathFactory', 'DadosVeiculos', '$resource', '$window', '$state', '$cookieStore', '$ionicModal', '$timeout', '$ionicSideMenuDelegate',
        function ($scope, pathFactory, DadosVeiculos, $resource, $window, $state, $cookieStore, $ionicModal, $timeout, $ionicSideMenuDelegate) {
            // Set user details
            $scope.user = $cookieStore.get('userInfo');

            // Form data for the carro modal
            $scope.car = {placa: '', renavam: ''};
            $scope.err = {placa: '', renavam: ''};

            // Logout user
            $scope.logout = function () {
                $cookieStore.remove("userInfo");
                $state.go('welcome');
                $window.location.reload();
            };

            // Create the carro modal that we will use later
            $ionicModal.fromTemplateUrl('partials/newCar.html', {
                scope: $scope
            }).then(function (modal) {
                $scope.modal = modal;
            });

            // Triggered in the carro modal to close it
            $scope.closeAddCar = function () {
                $scope.modal.hide();
            };

            // Open the carro modal
            $scope.newCar = function () {
                $scope.modal.show();
            };

            $scope.openmenu = function () {
                $ionicSideMenuDelegate.toggleLeft();
            };

            // Perform the carro action when the user submits the carro form
            $scope.doAdd = function () {
                if ($scope.car.placa.length == 7 && $scope.car.renavan != "") {
                    $scope.car.email = $scope.user.email;
                    DadosVeiculos.setVeiculo($scope.car);

                    var sendVeiculo = pathFactory.save({
                        "veiculo": $scope.car,
                        "method": "POST",
                        "headers": {
                            "X-AUTH-TOKEN": "1234"
                        }
                    }).$promise;

                    sendVeiculo
                        .then(function(data){
                            $scope.car = {placa: '', renavam: ''};
                            $scope.msg = 'Veículo adicionado com sucesso';
                        })
                        .catch(function(error) {
                            if (error.status == '400') {
                                $scope.msgErro = error.err;
                            }
                        });

                    // Simulate a carro delay. Remove this and replace with your carro
                    // code if using a carro system
                    $timeout(function () {
                        $scope.closeAddCar();
                    }, 500);

                    $scope.openmenu();
                }else {
                    if ($scope.car.placa.length == 7) {
                        $scope.err.placa = 'O numero da placa não contém 7 dígitos';
                    }else{$scope.err.placa = ''}

                    if ($scope.car.renavan != "") {
                        $scope.err.renavan = 'O numero do renavan não pode ficar vazio';
                    }else {$scope.err.renavan = ''}
                }
            };
        }])

        .controller('veiculosCtrl', ['$scope', 'pathFactory', 'DadosVeiculos', function ($scope, pathFactory, DadosVeiculos) {
            $scope.carros = DadosVeiculos.getVeiculos();

            $scope.excluiVeiculo = function(id) {
                var sendExclusaoPromise = pathFactory.query({
                    "veiculo": $scope.car,
                    "method": "DELETE",
                    "headers": {
                        "X-AUTH-TOKEN": "1234"
                    }
                })
            };

        }])

        .controller('veiculoCtrl', ['$scope', 'DadosVeiculos', function ($scope, DadosVeiculos) {
            $scope.dados = DadosVeiculos.getDados()
        }])

        .controller('dash', ['$scope', 'dadosUser', function($scope, dadosUser) {
            $scope.carros = dadosUser.getVeiculos();
            $scope.test = '';
        }]);
})();