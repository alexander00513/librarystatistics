(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationDeleteController',PublicationDeleteController);

    PublicationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Publication'];

    function PublicationDeleteController($uibModalInstance, entity, Publication) {
        var vm = this;
        vm.publication = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Publication.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
