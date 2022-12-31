import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCommunityComponent } from './update-community.component';

describe('UpdateCommunityComponent', () => {
  let component: UpdateCommunityComponent;
  let fixture: ComponentFixture<UpdateCommunityComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [UpdateCommunityComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UpdateCommunityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
