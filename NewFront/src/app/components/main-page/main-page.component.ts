import { Component, OnInit } from '@angular/core';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-main-page',
  templateUrl: './main-page.component.html',
  styleUrls: ['./main-page.component.css']
})
export class MainPageComponent implements OnInit {

  posts: Array<Post> = [];

  constructor(private postService: PostService) {
    this.postService.GetAll().subscribe(post => {
      this.posts = post;
      console.log(post);
    });
   }

  ngOnInit(): void {
  }

}
