import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TodonewComponent } from './todonew.component';

describe('TodonewComponent', () => {
  let component: TodonewComponent;
  let fixture: ComponentFixture<TodonewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TodonewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TodonewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
