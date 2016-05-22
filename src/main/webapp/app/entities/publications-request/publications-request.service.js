(function() {
    'use strict';
    angular
        .module('librarystatisticsApp')
        .factory('PublicationsRequest', PublicationsRequest);

    PublicationsRequest.$inject = ['$resource', 'DateUtils'];

    function PublicationsRequest ($resource, DateUtils) {
        var resourceUrl =  'api/publications-requests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.fromDate = DateUtils.convertDateTimeFromServer(data.fromDate);
                        data.toDate = DateUtils.convertDateTimeFromServer(data.toDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
