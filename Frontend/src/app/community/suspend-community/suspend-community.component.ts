import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/service/community.service';

@Component({
  selector: 'app-suspend-community',
  templateUrl: './suspend-community.component.html',
  styleUrls: ['./suspend-community.component.css']
})
export class SuspendCommunityComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  community_id: number;
  community: Community;

  constructor(private communityService: CommunityService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.community = new Community();

    this.formGroup = this.formBuilder.group({
      suspended_reason: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(30)]]
    })

    this.community_id = this.route.snapshot.params['community_id'];
    this.communityService.getCommunity(this.community_id)
      .subscribe(data => {
        console.log(data)
        this.community = data;
      }, error => console.log(error));
  }

  get f() {
    return this.formGroup.controls;
  }

  suspend() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.communityService.suspendCommunity(this.community_id, this.community).subscribe(
      data => {
        console.log(data);
        this.router.navigate(['/communities']);
      },
      error => {
        console.log(error);
      });
  }


}
