import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageContentsComponent } from './message-contents.component';

describe('MessageContentsComponent', () => {
  let component: MessageContentsComponent;
  let fixture: ComponentFixture<MessageContentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MessageContentsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MessageContentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
