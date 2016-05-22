(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('LibraryDialogController', LibraryDialogController);

    LibraryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Library', 'Borough', 'Equipment', 'Publication', 'Subscription', 'PublicationsRequest', 'Event', 'Website', 'Person'];

    function LibraryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Library, Borough, Equipment, Publication, Subscription, PublicationsRequest, Event, Website, Person) {
        var vm = this;
        vm.library = entity;
        vm.boroughs = Borough.query();
        vm.equipment = Equipment.query();
        vm.publications = Publication.query();
        vm.subscriptions = Subscription.query();
        vm.publicationsrequests = PublicationsRequest.query();
        vm.events = Event.query();
        vm.websites = Website.query();
        vm.people = Person.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:libraryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.library.id !== null) {
                Library.update(vm.library, onSaveSuccess, onSaveError);
            } else {
                Library.save(vm.library, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
