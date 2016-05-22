'use strict';

describe('Controller Tests', function() {

    describe('Borough Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBorough, MockLibrary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBorough = jasmine.createSpy('MockBorough');
            MockLibrary = jasmine.createSpy('MockLibrary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Borough': MockBorough,
                'Library': MockLibrary
            };
            createController = function() {
                $injector.get('$controller')("BoroughDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:boroughUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
