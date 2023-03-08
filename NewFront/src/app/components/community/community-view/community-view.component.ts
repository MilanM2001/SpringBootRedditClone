import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { Post } from 'src/app/model/post.model';
import { Rule } from 'src/app/model/rule.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-community-view',
  templateUrl: './community-view.component.html',
  styleUrls: ['./community-view.component.css']
})
export class CommunityViewComponent implements OnInit {

  @Input() community: Community = new Community();
  posts: Post[] = [];
  rules: Rule[] = [];

  communityId: number = Number(this.route.snapshot.paramMap.get('communityId'));

  constructor(private communityService: CommunityService,
              private route: ActivatedRoute,
              private router: Router) { }

  ngOnInit() {
    this.communityService.GetSingle(this.communityId)
      .subscribe({
        next: (data: Community) => {
          this.community = data as Community;
          console.log(this.community);
        },
        error: (error) => {
          console.log(error);
          this.router.navigate(['/404']);
        }
      });

    this.communityService.GetCommunityPosts(this.communityId)
      .subscribe({
        next: (data: Post[]) => {
          this.posts = data as Post[];
          console.log(this.posts);
        },
        error: (error) => {
          console.log(error);
        }
      });

    this.communityService.GetCommunityRules(this.communityId)
      .subscribe({
        next: (data: Rule[]) => {
          this.rules = data as Rule[];
          console.log(this.rules);
        },
        error: (error) => {
          console.log(error);
        }
      });

  }

  navigateTo(value: string){
    this.router.navigate([value, this.community.communityId]);
  }

}
