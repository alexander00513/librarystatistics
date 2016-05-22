(function() {
    'use strict';
    angular
        .module('librarystatisticsApp')
        .factory('Subscription', Subscription);

    Subscription.$inject = ['$resource', 'DateUtils'];

    function Subscription ($resource, DateUtils) {
        var resourceUrl =  'api/subscriptions/:id';

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
