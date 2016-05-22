(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('BoroughDetailController', BoroughDetailController);

    BoroughDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Borough', 'Library'];

    function BoroughDetailController($scope, $rootScope, $stateParams, entity, Borough, Library) {
        var vm = this;
        vm.borough = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:boroughUpdate', function(event, result) {
            vm.borough = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
