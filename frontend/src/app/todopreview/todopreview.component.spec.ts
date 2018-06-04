import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TodopreviewComponent } from './todopreview.component';

describe('TodopreviewComponent', () => {
  let component: TodopreviewComponent;
  let fixture: ComponentFixture<TodopreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TodopreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TodopreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
