import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, RequiredValidator, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { UserService } from 'src/app/service';
import { CommunityService } from 'src/app/service/community.service';
import { PostService } from 'src/app/service/post.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  @Output() savePostEvent = new EventEmitter<Comment>()

  currentCommunity = null;

  constructor(private communityService: CommunityService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.getCommunity(this.route.snapshot.paramMap.get('community_id'));
    this.formGroup = this.formBuilder.group({
      title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(85)]]
    })
  }

  get f() { return this.formGroup.controls; }

  onSubmit() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let addPost: Post = new Post();

    addPost.title = this.formGroup.get('title').value;
    addPost.text = this.formGroup.get('text').value;

    this.communityService.addPost(Number(this.route.snapshot.paramMap.get('community_id')), addPost).subscribe(
      data => {
        console.log(data);
        this.savePostEvent.emit(this.currentCommunity);
        this.formGroup.controls['title'].reset();
        this.formGroup.controls['text'].reset();
      }, error => {
        console.log(error);
      });
  }

  getCommunity(community_id) {
    this.communityService.getCommunity(community_id).subscribe(
      data => {
        this.currentCommunity = data;
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
