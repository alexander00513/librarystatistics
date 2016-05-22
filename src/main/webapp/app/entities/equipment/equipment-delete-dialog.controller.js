(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('EquipmentDeleteController',EquipmentDeleteController);

    EquipmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Equipment'];

    function EquipmentDeleteController($uibModalInstance, entity, Equipment) {
        var vm = this;
        vm.equipment = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Equipment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
