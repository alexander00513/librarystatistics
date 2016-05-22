(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationDialogController', PublicationDialogController);

    PublicationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Publication', 'Library'];

    function PublicationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Publication, Library) {
        var vm = this;
        vm.publication = entity;
        vm.libraries = Library.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:publicationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.publication.id !== null) {
                Publication.update(vm.publication, onSaveSuccess, onSaveError);
            } else {
                Publication.save(vm.publication, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
