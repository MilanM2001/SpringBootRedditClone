import { Component, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { EventEmitter } from 'protractor';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-update-post',
  templateUrl: './update-post.component.html',
  styleUrls: ['./update-post.component.css']
})
export class UpdatePostComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  post_id: number;
  post: Post;

  constructor(private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.post = new Post();

    this.formGroup = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(120)]]
    })

    this.post_id = this.route.snapshot.params['post_id'];
    this.postService.getPost(this.post_id)
      .subscribe(data => {
        console.log(data)
        this.post = data;
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

    this.postService.updatePost(this.post_id, this.post).subscribe(
      data => {
        console.log(data);
        this.router.navigateByUrl('/view-post/' + this.post_id);
      },
      error => {
        console.log(error);
      });
  }

}
