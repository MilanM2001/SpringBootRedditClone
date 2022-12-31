import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Comment } from 'src/app/model/comment.model';
import { UserService } from 'src/app/service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-create-comment',
  templateUrl: './create-comment.component.html',
  styleUrls: ['./create-comment.component.css']
})
export class CreateCommentComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  @Output() saveCommentEvent = new EventEmitter<Comment>()

  currentPost = null; 

  constructor(private postService: PostService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.getPost(this.route.snapshot.paramMap.get('post_id'));
    this.formGroup = this.formBuilder.group({
      text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]]
    })
  }

  get f() { return this.formGroup.controls; }

  onSubmit() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let addComment: Comment = new Comment();

    addComment.text = this.formGroup.get('text').value;

    this.postService.addComment(Number(this.route.snapshot.paramMap.get('post_id')), addComment).subscribe(
      data => {
        console.log(data);
        this.saveCommentEvent.emit(this.currentPost);
        this.formGroup.controls['text'].reset();
        // this.router.navigateByUrl('/view-post/' + this.currentPost.post_id)
        // window.location.reload();
      }, error => {
        console.log(error);
      });
  }

  getPost(post_id) {
    this.postService.getPost(post_id).subscribe(
      data => {
        this.currentPost = data;
        console.log(data);
      },
      error => {
        console.log(error);
      });
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

}
