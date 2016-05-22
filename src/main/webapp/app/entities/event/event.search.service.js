(function() {
    'use strict';

    angular
        .module('librarystatisticsApp')
        .factory('EventSearch', EventSearch);

    EventSearch.$inject = ['$resource'];

    function EventSearch($resource) {
        var resourceUrl =  'api/_search/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
