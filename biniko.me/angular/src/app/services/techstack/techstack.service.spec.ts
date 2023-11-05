import { TestBed } from '@angular/core/testing';

import { Tech } from './tech';
import { TechstackService } from './techstack.service';

fdescribe('TechstackService', () => {
  let techstackService: TechstackService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TechstackService]
    });
    techstackService = TestBed.get(TechstackService);
  });

  it('should be created', () => {
    expect(techstackService).toBeTruthy();
  });

  describe('getTechstack', () => {
    it('should return a list of techstack', () => {
      const expectedResponse = [
        {
          'role': 'Frontend',
          'name': 'Angular / Typescript',
          'tags': ['Karma', 'Jasmine', 'Cypress']
        }
      ];

      let actualResponse = techstackService.getTechstack();

      expect(actualResponse).toEqual(expectedResponse);
    });
  });
});
