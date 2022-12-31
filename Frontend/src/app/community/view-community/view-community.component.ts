import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { Rule } from 'src/app/model/rule.model';
import { UserService } from 'src/app/service';
import { CommunityService } from 'src/app/service/community.service';

@Component({
  selector: 'app-view-community',
  templateUrl: './view-community.component.html',
  styleUrls: ['./view-community.component.css']
})
export class ViewCommunityComponent implements OnInit {

  @Input() community: Community = new Community();
  posts: Post[] = []
  rules: Rule[] = []
  flairs: Flair[] = []



  constructor(private communityService: CommunityService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.getCommunity(this.route.snapshot.paramMap.get('community_id'));
    console.log(this.community);
    this.communityService.getCommunityPosts(Number(this.route.snapshot.paramMap.get('community_id'))).subscribe(
      data => {
        this.posts = data as Post[];
      });
    this.communityService.getCommunityRules(Number(this.route.snapshot.paramMap.get('community_id'))).subscribe(
      data => {
        this.rules = data as Rule[];
      });
    this.communityService.getCommunityFlairs(Number(this.route.snapshot.paramMap.get('community_id'))).subscribe(
      data => {
        this.flairs = data as Flair[];
      });
  }

  getCommunity(community_id) {
    this.communityService.getCommunity(community_id).subscribe(
      data => {
        this.community = data;
      },
      error => {
        console.log(error);
      });
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

}
