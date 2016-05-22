(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('WebsiteDialogController', WebsiteDialogController);

    WebsiteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Website', 'Library'];

    function WebsiteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Website, Library) {
        var vm = this;
        vm.website = entity;
        vm.libraries = Library.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:websiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.website.id !== null) {
                Website.update(vm.website, onSaveSuccess, onSaveError);
            } else {
                Website.save(vm.website, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
