import { Component, OnInit } from '@angular/core';
import { Community } from 'src/app/model/community.model';
import { CommunityService } from '../../service/community.service';

@Component({
  selector: 'app-community-list',
  templateUrl: './community-list.component.html',
  styleUrls: ['./community-list.component.css']
})
export class CommunityListComponent implements OnInit {

  communities: Community[] = [];

  constructor(private communityService: CommunityService) { }

  ngOnInit(): void {
    this.communityService.getAll().subscribe(data => {
      this.communities = data as Community[];
      console.log(data);
    })
  }

}
