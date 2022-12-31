import { Component, Input, OnInit } from '@angular/core';
import { Community } from 'src/app/model/community.model';
import { UserService } from 'src/app/service';


@Component({
  selector: 'app-community-item',
  templateUrl: './community-item.component.html',
  styleUrls: ['./community-item.component.css']
})
export class CommunityItemComponent implements OnInit {

  @Input() community: Community = new Community();
  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

  role() {
    const user = this.userService.getCurrent();
    return user.role;
  }

  isUser() {
    return this.role() === 'USER';
  }

  isAdmin() {
    return this.role() === 'ADMIN';
  }

  user_id() {
    const user = this.userService.getCurrent();
    return user.user_id;
  }

  // community_user_id() {
  //   this.community.moderators.user_id;
  // }

  // isModeratedByUser() {
  //   this.user_id() === this.community_user_id();
  // }

  isModerator(): boolean {
    for (let moderator of this.community.moderators) {
      if (moderator.user_id === this.user_id()) {
        return true;
      }
    }
    return false;
  }

}
