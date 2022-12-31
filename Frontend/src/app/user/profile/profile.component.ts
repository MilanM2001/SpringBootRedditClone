import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/model/post.model';
import { ReactionType } from 'src/app/model/ReactionType.enum';
import { UserService } from 'src/app/service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  currentUser = this.userService.getCurrent();;
  posts: Post[] = []

  constructor(private postService: PostService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.getUser(this.currentUser.user_id);
    this.userService.getUserPosts(Number(this.currentUser.user_id)).subscribe(
      data => {
        this.posts = data as Post[];
      });
  }

  getUser(user_id) {
    this.userService.getUser(user_id).subscribe(
      data => {
        this.currentUser = data;
        console.log(data);
      },
      error => {
        console.log(error)
      });
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

  userKarma(): number {
    let karma = 0;
    for (let reaction of this.currentUser.reactions) {
      if (reaction.reaction_type.toString() === ReactionType[ReactionType.UPVOTE]) {
        karma++
      } else {
        karma--
      }
    }
    return karma;
  }


}
