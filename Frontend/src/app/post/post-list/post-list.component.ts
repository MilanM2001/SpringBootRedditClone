import { Component, Input, OnInit } from '@angular/core';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/service/post.service';


@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {

  @Input()
  posts: Post[] = [];

  constructor(private postService: PostService) { }

  ngOnInit(): void {
    //   this.postService.getAll().subscribe(data => {
    //   this.posts = data as Post[];
    //   console.log(data);
    // })
  }

}
