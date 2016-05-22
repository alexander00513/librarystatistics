(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .controller('PublicationsRequestDeleteController',PublicationsRequestDeleteController);

    PublicationsRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'PublicationsRequest'];

    function PublicationsRequestDeleteController($uibModalInstance, entity, PublicationsRequest) {
        var vm = this;
        vm.publicationsRequest = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            PublicationsRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
