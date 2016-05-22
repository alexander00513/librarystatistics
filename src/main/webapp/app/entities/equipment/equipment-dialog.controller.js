(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('EquipmentDialogController', EquipmentDialogController);

    EquipmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Equipment', 'Library'];

    function EquipmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Equipment, Library) {
        var vm = this;
        vm.equipment = entity;
        vm.libraries = Library.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:equipmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.equipment.id !== null) {
                Equipment.update(vm.equipment, onSaveSuccess, onSaveError);
            } else {
                Equipment.save(vm.equipment, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
