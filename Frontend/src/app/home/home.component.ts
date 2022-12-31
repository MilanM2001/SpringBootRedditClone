import { Component, OnInit } from '@angular/core';
import { Flair } from '../model/flair.model';
import { Post } from '../model/post.model'
import { FlairService } from '../service/flair.service';
import { PostService } from '../service/post.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  posts: Array<Post> = [];
  flairs: Array<Flair> = [];

  constructor(private postService: PostService) {
    this.postService.getAll().subscribe(post => {
      this.posts = post;
      console.log(post);
    });
  }

  ngOnInit(): void {
  }

}
