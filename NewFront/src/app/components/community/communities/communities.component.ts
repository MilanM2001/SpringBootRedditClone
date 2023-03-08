import { Component, OnInit } from '@angular/core';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-communities',
  templateUrl: './communities.component.html',
  styleUrls: ['./communities.component.css']
})
export class CommunitiesComponent implements OnInit {

  communities: Array<Community> = [];

  constructor(private communityService: CommunityService) {
    this.communityService.GetAll().subscribe(community => {
      this.communities = community;
      console.log(community);
    })
   }

  ngOnInit(): void {
  }

}
