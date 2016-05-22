'use strict';

describe('Controller Tests', function() {

    describe('Equipment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEquipment, MockLibrary;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEquipment = jasmine.createSpy('MockEquipment');
            MockLibrary = jasmine.createSpy('MockLibrary');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Equipment': MockEquipment,
                'Library': MockLibrary
            };
            createController = function() {
                $injector.get('$controller')("EquipmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'librarystatisticsApp:equipmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
