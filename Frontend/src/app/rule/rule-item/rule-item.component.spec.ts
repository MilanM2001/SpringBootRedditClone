import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleItemComponent } from './rule-item.component';

describe('RuleItemComponent', () => {
  let component: RuleItemComponent;
  let fixture: ComponentFixture<RuleItemComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [RuleItemComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
