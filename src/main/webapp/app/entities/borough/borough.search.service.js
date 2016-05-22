(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('BoroughSearch', BoroughSearch);

    BoroughSearch.$inject = ['$resource'];

    function BoroughSearch($resource) {
        var resourceUrl =  'api/_search/boroughs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
