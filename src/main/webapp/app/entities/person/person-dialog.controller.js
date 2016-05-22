(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PersonDialogController', PersonDialogController);

    PersonDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Person', 'Library', 'Event'];

    function PersonDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Person, Library, Event) {
        var vm = this;
        vm.person = entity;
        vm.libraries = Library.query();
        vm.events = Event.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:personUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.person.id !== null) {
                Person.update(vm.person, onSaveSuccess, onSaveError);
            } else {
                Person.save(vm.person, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
