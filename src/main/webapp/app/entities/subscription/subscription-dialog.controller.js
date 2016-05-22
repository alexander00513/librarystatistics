(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('SubscriptionDialogController', SubscriptionDialogController);

    SubscriptionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Subscription', 'Library', 'Person', 'Publication'];

    function SubscriptionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Subscription, Library, Person, Publication) {
        var vm = this;
        vm.subscription = entity;
        vm.libraries = Library.query();
        vm.persons = Person.query({filter: 'subscription-is-null'});
        $q.all([vm.subscription.$promise, vm.persons.$promise]).then(function() {
            if (!vm.subscription.personId) {
                return $q.reject();
            }
            return Person.get({id : vm.subscription.personId}).$promise;
        }).then(function(person) {
            vm.people.push(person);
        });
        vm.publications = Publication.query({filter: 'subscription-is-null'});
        $q.all([vm.subscription.$promise, vm.publications.$promise]).then(function() {
            if (!vm.subscription.publicationId) {
                return $q.reject();
            }
            return Publication.get({id : vm.subscription.publicationId}).$promise;
        }).then(function(publication) {
            vm.publications.push(publication);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        var onSaveSuccess = function (result) {
            $scope.$emit('librarystatisticsApp:subscriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.subscription.id !== null) {
                Subscription.update(vm.subscription, onSaveSuccess, onSaveError);
            } else {
                Subscription.save(vm.subscription, onSaveSuccess, onSaveError);
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
