import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateReactionDTO } from 'src/app/model/CreateReactionDTO';
import { Post } from 'src/app/model/post.model';
import { Reaction } from 'src/app/model/reaction.model';
import { ReactionType } from 'src/app/model/ReactionType.enum';
import { AuthService, UserService } from 'src/app/service';
import { PostService } from 'src/app/service/post.service';
import { ReactionService } from 'src/app/service/reaction.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {
  @Input() post: Post = new Post();
  @Input() comment: Comment = new Comment();

  @Output() upvotePostEvent = new EventEmitter<Reaction[]>()

  comments: Comment[] = []

  constructor(private postService: PostService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private reactionService: ReactionService) { }

  ngOnInit() {
    this.getPost(this.route.snapshot.paramMap.get('post_id'));
    this.postService.getPostComments(Number(this.route.snapshot.paramMap.get('post_id'))).subscribe(
      data => {
        this.comments = data as Comment[];
        console.log(data);
      });
  }

  getPost(post_id) {
    this.postService.getPost(post_id).subscribe(
      data => {
        this.post = data;
      },
      error => {
        console.log(error)
      });
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

  user_id() {
    const user = this.userService.getCurrent();
    return user.user_id;
  }

  username():string {
    const user = this.userService.getCurrent();
    return user.username;
  }

  isByUser() {
    return this.user_id();
  }

  deletePost() {
    this.postService.delete(this.post.post_id)
      .subscribe(
        data => {
          console.log(data);
          this.router.navigateByUrl('/view-community/' + this.post.community.community_id);
        },
        error => {
          console.log(error);
        });
  }

  upvotePost() {
    if(!this.reactedOnPost()) {
      let reaction = new CreateReactionDTO();
      reaction.reaction_type = ReactionType[ReactionType.UPVOTE];
      reaction.post_id = this.post.post_id;
      this.reactionService.create(reaction).subscribe((reaction: Reaction) => {
        console.log("Upvoted Post");
        console.log(reaction);
        window.location.reload();
        // this.upvotePostEvent.emit();
      }, (error) => {
        console.log("Not Created");
        console.log(error);
      });
    } else {
      alert("You have already reacted to this Post!")
    }
  }

  downvotePost() {
    if(!this.reactedOnPost()) {
      let reaction = new CreateReactionDTO();
      reaction.reaction_type = ReactionType[ReactionType.DOWNVOTE];
      reaction.post_id = this.post.post_id;
      this.reactionService.create(reaction).subscribe((reaction: Reaction) => {
        console.log("Downvoted Post");
        console.log(reaction);
        window.location.reload();
      }, (error) => {
        console.log("Not Created");
        console.log(error);
      });
    } else { 
      alert("You have already reacted to this Post!")
    }
  }

  postKarma(): number {
    let karma = 0;
    for (let reaction of this.post.reactions) {
      if (reaction.reaction_type.toString() === ReactionType[ReactionType.UPVOTE]) {
        karma++
      } else {
        karma--
      } 
    }
    return karma;
  }

  reactedOnPost(): boolean {
    for (let reaction of this.post.reactions) {
      if (reaction.user.username === this.username()) {
        return true
      }
    }
    return false;
  }

}