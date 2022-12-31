import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/service/community.service';

@Component({
  selector: 'app-update-community',
  templateUrl: './update-community.component.html',
  styleUrls: ['./update-community.component.css']
})
export class UpdateCommunityComponent implements OnInit {
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
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(85)]]
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

  update() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.communityService.updateCommunity(this.community_id, this.community).subscribe(
      data => {
        console.log(data);
        this.router.navigateByUrl('/communities');
      },
      error => {
        console.log(error);
      });
  }

}
