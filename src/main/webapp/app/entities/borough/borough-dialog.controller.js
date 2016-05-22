(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('BoroughDialogController', BoroughDialogController);

    BoroughDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Borough', 'Library'];

    function BoroughDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Borough, Library) {
        var vm = this;
        vm.borough = entity;
        vm.libraries = Library.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:boroughUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.borough.id !== null) {
                Borough.update(vm.borough, onSaveSuccess, onSaveError);
            } else {
                Borough.save(vm.borough, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
