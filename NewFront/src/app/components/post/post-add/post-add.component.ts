import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AddPostDTO } from 'src/app/dto/addPostDTO';
import { Community } from 'src/app/model/community.model';
import { Flair } from 'src/app/model/flair.model';
import { Post } from 'src/app/model/post.model';
import { CommunityService } from 'src/app/services/community.service';

@Component({
  selector: 'app-post-add',
  templateUrl: './post-add.component.html',
  styleUrls: ['./post-add.component.css']
})
export class PostAddComponent implements OnInit {

  formGroup: FormGroup = new FormGroup({
    title: new FormControl(''),
    text: new FormControl(''),
    flairId: new FormControl('')
  });

  community: Community = new Community();
  flairs: Flair[] =  [];

  constructor(private communityService: CommunityService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder) { }
  
  // @ts-ignore
  formGroup: FormGroup;
  submitted = false;
  
  ngOnInit(): void {
    this.communityService.GetSingle(Number(this.route.snapshot.paramMap.get('communityId')))
      .subscribe({
        next: (data: Community) => {
          this.community = data as Community;
          console.log(this.community);
        },
        error: (error) => {
          console.log(error);
        }
      });

    this.communityService.GetCommunityFlairs(Number(this.route.snapshot.paramMap.get('communityId')))
      .subscribe({
        next: (data: Flair[]) => {
          this.flairs = data as Flair[];
          console.log(this.flairs);
        }
      })

      this.formGroup = this.formBuilder.group({
        title: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(35)]],
        text: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(300)]],
        flairId: ['', [Validators.required]]
      })
  }

  get f(): { [key: string]: AbstractControl } {
    return this.formGroup.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let addPost: AddPostDTO = new AddPostDTO();

    addPost.title = this.formGroup.get('title')?.value;
    addPost.text = this.formGroup.get('text')?.value;
    addPost.flairId = this.formGroup.get('flairId')?.value;

    this.communityService.AddPost(Number(this.route.snapshot.paramMap.get('communityId')), addPost)
      .subscribe({
        next: (data: Post) => {
          console.log(data);
          history.back();
        },
        error: (error) => {
          console.log(error);
        }
      });
  }

}
