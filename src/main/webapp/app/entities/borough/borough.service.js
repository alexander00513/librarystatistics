(function() {
    'use strict';
    angular
        .module('librarystatisticsApp')
        .factory('Borough', Borough);

    Borough.$inject = ['$resource'];

    function Borough ($resource) {
        var resourceUrl =  'api/boroughs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
