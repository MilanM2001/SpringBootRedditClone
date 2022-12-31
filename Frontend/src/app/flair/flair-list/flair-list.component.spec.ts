import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlairListComponent } from './flair-list.component';

describe('FlairListComponent', () => {
  let component: FlairListComponent;
  let fixture: ComponentFixture<FlairListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FlairListComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlairListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
