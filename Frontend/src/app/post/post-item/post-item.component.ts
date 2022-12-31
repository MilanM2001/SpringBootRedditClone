import { Component, Input, OnInit } from '@angular/core';
import { Post } from 'src/app/model/post.model';
import { UserService } from 'src/app/service';

@Component({
  selector: 'app-post-item',
  templateUrl: './post-item.component.html',
  styleUrls: ['./post-item.component.css']
})
export class PostItemComponent implements OnInit {

  @Input() post: Post = new Post();
  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

  user_id() {
    const user = this.userService.getCurrent();
    return user.user_id;
  }

  isByUser() {
    return this.user_id() === this.post.user.user_id;
  }

}
