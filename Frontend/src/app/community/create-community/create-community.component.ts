import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Flair } from 'src/app/model/flair.model';
import { Rule } from 'src/app/model/rule.model';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from '../../service/community.service';
import { Subject } from 'rxjs/Subject';
import { takeUntil } from 'rxjs/operators';

interface DisplayMessage {
  msgType: string;
  msgBody: string;
}

@Component({
  selector: 'app-create-community',
  templateUrl: './create-community.component.html',
  styleUrls: ['./create-community.component.css']
})
export class CreateCommunityComponent implements OnInit {

  flairs: Flair[] = [];
  rules: Rule[] = [];

  formGroup: FormGroup;
  submitted = false;

  notification: DisplayMessage;

  returnUrl: string;
  private ngUnsubscribe: Subject<void> = new Subject<void>();

  constructor(
    private formBuilder: FormBuilder,
    private communityService: CommunityService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit() {
    this.route.params
      .pipe(takeUntil(this.ngUnsubscribe))
      .subscribe((params: DisplayMessage) => {
        this.notification = params;
      });

    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    this.formGroup = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(85)]],
      flairs: [],
      rules: []
    });
  }

  ngOnDestroy() {
    this.ngUnsubscribe.next();
    this.ngUnsubscribe.complete();
  }

  get f() { return this.formGroup.controls; }

  addFlair() {
    let addFlair: Flair = new Flair();
    addFlair.name = this.formGroup.get("flairs").value;
    this.flairs.push(addFlair);
    this.formGroup.controls['flairs'].reset();
  }

  addRule() {
    let addRule: Rule = new Rule();
    addRule.description = this.formGroup.value.rules;
    this.rules.push(addRule);
    this.formGroup.controls['rules'].reset();
  }

  onSubmit() {
    this.notification = undefined;
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let addCommunity: Community = new Community();

    addCommunity.name = this.formGroup.get("name").value;
    addCommunity.description = this.formGroup.get("description").value;
    addCommunity.flairs = this.flairs;
    addCommunity.rules = this.rules;

    this.communityService.addCommunity(addCommunity).subscribe(data => {
      if (data) {
        this.router.navigate(['communities'])
      }
    }, error => {
      this.submitted = false;
      console.log('Create Community Error');
      this.notification = { msgType: 'error', msgBody: 'Name Taken' };
    });

  }

}
