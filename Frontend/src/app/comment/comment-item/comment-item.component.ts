import { Component, Input, OnInit } from '@angular/core';
import { Comment } from 'src/app/model/comment.model';
import { CreateReactionDTO } from 'src/app/model/CreateReactionDTO';
import { Reaction } from 'src/app/model/reaction.model';
import { ReactionType } from 'src/app/model/ReactionType.enum';
import { UserService } from 'src/app/service';
import { CommentService } from 'src/app/service/comment.service';
import { ReactionService } from 'src/app/service/reaction.service';

@Component({
  selector: 'app-comment-item',
  templateUrl: './comment-item.component.html',
  styleUrls: ['./comment-item.component.css']
})
export class CommentItemComponent implements OnInit {
  @Input() comment: Comment = new Comment();

  constructor(private commentService: CommentService,
    private userService: UserService,
    private reactionService: ReactionService) { }

  ngOnInit() {

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

  deleteComment() {
    this.commentService.delete(this.comment.comment_id)
      .subscribe(
        data => {
          window.location.reload();
          console.log(data);
        },
        error => {
          console.log(error);
        });
  }

  upvoteComment() {
    if(!this.reactedOnComment()) {
      let reaction = new CreateReactionDTO();
      reaction.reaction_type = ReactionType[ReactionType.UPVOTE];
      reaction.comment_id = this.comment.comment_id;
      this.reactionService.create(reaction).subscribe((reaction: Reaction) => {
        console.log("Upvoted Comment");
        console.log(reaction);
        window.location.reload();
    }, (error) => {
      console.log("Not Created");
      console.log(error);
    });
  } else {
    alert("You have already reacted to this Comment!")
  }
}

  downvoteComment() {
    if(!this.reactedOnComment()) {
    let reaction = new CreateReactionDTO();
    reaction.reaction_type = ReactionType[ReactionType.DOWNVOTE];
    reaction.comment_id = this.comment.comment_id;
    this.reactionService.create(reaction).subscribe((reaction: Reaction) => {
      console.log("Downvoted Comment");
      console.log(reaction);
      window.location.reload();
    }, (error) => {
      console.log("Not Created");
      console.log(error);
    });
    } else {
      alert("You have already reacted to this Comment!")
    }
  }

  commentKarma(): number {
    let commentKarma = 0;
    for (let reaction of this.comment.reactions) {
      if (reaction.reaction_type.toString() === ReactionType[ReactionType.UPVOTE]) {
        commentKarma++
      } else {
        commentKarma--
      }
    }
    return commentKarma;
  }

  reactedOnComment(): boolean {
    for (let reaction of this.comment.reactions) {
      if (reaction.user.username === this.username()) {
        return true
      }
    }
    return false;
  }

}

