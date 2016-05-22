'use strict';

describe('Controller Tests', function() {

    describe('Publication Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPublication, MockLibrary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPublication = jasmine.createSpy('MockPublication');
            MockLibrary = jasmine.createSpy('MockLibrary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Publication': MockPublication,
                'Library': MockLibrary
            };
            createController = function() {
                $injector.get('$controller')("PublicationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:publicationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
