// src/app/services/techstack/techstack.service.ts

import { Injectable } from '@angular/core';
import { Tech } from './tech';

@Injectable({
  providedIn: 'root'
})
export class TechstackService {

  constructor() { }

  getTechstack(): Tech[] {
    return [
      {
        'role': 'Frontend',
        'name': 'Angular / Typescript',
        'tags': ['Karma', 'Jasmine', 'Cypress']
      }
    ];
  }
}
