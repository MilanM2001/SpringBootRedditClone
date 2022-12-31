import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Comment } from 'src/app/model/comment.model';
import { CommentService } from 'src/app/service/comment.service';

@Component({
  selector: 'app-update-comment',
  templateUrl: './update-comment.component.html',
  styleUrls: ['./update-comment.component.css']
})
export class UpdateCommentComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  comment_id: number;
  comment: Comment;

  constructor(private commentService: CommentService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.comment = new Comment();

    this.formGroup = this.formBuilder.group({
      text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]]
    })

    this.comment_id = this.route.snapshot.params['comment_id'];
    this.commentService.getComment(this.comment_id)
      .subscribe(data => {
        console.log(data)
        this.comment = data;
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

    this.commentService.updateComment(this.comment_id, this.comment).subscribe(
      data => {
        console.log(data);
        window.history.back();
      },
      error => {
        console.log(error);
      });
  }

  cancelUpdateButton() {
    window.history.back();
  }

}
