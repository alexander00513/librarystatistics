(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationsRequestDialogController', PublicationsRequestDialogController);

    PublicationsRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PublicationsRequest', 'Library', 'Person', 'Publication'];

    function PublicationsRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PublicationsRequest, Library, Person, Publication) {
        var vm = this;
        vm.publicationsRequest = entity;
        vm.libraries = Library.query();
        vm.persons = Person.query({filter: 'publicationsrequest-is-null'});
        $q.all([vm.publicationsRequest.$promise, vm.persons.$promise]).then(function() {
            if (!vm.publicationsRequest.personId) {
                return $q.reject();
            }
            return Person.get({id : vm.publicationsRequest.personId}).$promise;
        }).then(function(person) {
            vm.people.push(person);
        });
        vm.publications = Publication.query({filter: 'publicationsrequest-is-null'});
        $q.all([vm.publicationsRequest.$promise, vm.publications.$promise]).then(function() {
            if (!vm.publicationsRequest.publicationId) {
                return $q.reject();
            }
            return Publication.get({id : vm.publicationsRequest.publicationId}).$promise;
        }).then(function(publication) {
            vm.publications.push(publication);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:publicationsRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.publicationsRequest.id !== null) {
                PublicationsRequest.update(vm.publicationsRequest, onSaveSuccess, onSaveError);
            } else {
                PublicationsRequest.save(vm.publicationsRequest, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.datePickerOpenStatus = {};
        vm.datePickerOpenStatus.fromDate = false;
        vm.datePickerOpenStatus.toDate = false;

        vm.openCalendar = function(date) {
            vm.datePickerOpenStatus[date] = true;
        };
    }
})();
