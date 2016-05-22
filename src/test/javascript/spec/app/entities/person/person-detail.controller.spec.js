'use strict';

describe('Controller Tests', function() {

    describe('Person Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPerson, MockLibrary, MockEvent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPerson = jasmine.createSpy('MockPerson');
            MockLibrary = jasmine.createSpy('MockLibrary');
            MockEvent = jasmine.createSpy('MockEvent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Person': MockPerson,
                'Library': MockLibrary,
                'Event': MockEvent
            };
            createController = function() {
                $injector.get('$controller')("PersonDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:personUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
