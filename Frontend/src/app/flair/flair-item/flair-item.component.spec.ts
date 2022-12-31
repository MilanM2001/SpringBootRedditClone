import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FlairItemComponent } from './flair-item.component';

describe('FlairItemComponent', () => {
  let component: FlairItemComponent;
  let fixture: ComponentFixture<FlairItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FlairItemComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FlairItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
