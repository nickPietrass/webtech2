import { TestBed, inject } from '@angular/core/testing';

import { TodomanagerService } from './todomanager.service';

describe('TodomanagerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TodomanagerService]
    });
  });

  it('should be created', inject([TodomanagerService], (service: TodomanagerService) => {
    expect(service).toBeTruthy();
  }));
});
