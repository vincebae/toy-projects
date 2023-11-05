// src/app/services/auth/auth.service.spec.ts

import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';

import { AuthService } from './auth.service';

fdescribe('AuthService', () => {
  let authService: AuthService;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    authService = TestBed.get(AuthService);
    http = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('signup', () => {
    it('should return a user object with a valid username and password', () => {
      const user = {'username': 'myUser', 'password': 'password' };
      const expectedResponse = {
        '__v': 0,
        'username': 'myUser',
        'password': 'encrypted password',
        'name': '',
        '_id': 'generated id'
      };

      let actualResponse: any;
      authService.signup(user).subscribe(res => {
        actualResponse = res;
      });

      http.expectOne('https://localhost:4200/api/users').flush(expectedResponse);
      expect(actualResponse).toEqual(expectedResponse);
      http.verify();
    });
  });
});
