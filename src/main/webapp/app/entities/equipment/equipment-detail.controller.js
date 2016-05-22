(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('EquipmentDetailController', EquipmentDetailController);

    EquipmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Equipment', 'Library'];

    function EquipmentDetailController($scope, $rootScope, $stateParams, entity, Equipment, Library) {
        var vm = this;
        vm.equipment = entity;
        
        var unsubscribe = $rootScope.$on('librarystatisticsApp:equipmentUpdate', function(event, result) {
            vm.equipment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
