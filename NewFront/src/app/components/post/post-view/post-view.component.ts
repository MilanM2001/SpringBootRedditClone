import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Post } from 'src/app/model/post.model';
import { PostService } from 'src/app/services/post.service';

@Component({
  selector: 'app-post-view',
  templateUrl: './post-view.component.html',
  styleUrls: ['./post-view.component.css']
})
export class PostViewComponent implements OnInit {

  post: Post = new Post();

  constructor(private postService: PostService,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location) { }

  ngOnInit(): void {
    this.postService.GetSingle(Number(this.route.snapshot.paramMap.get('postId')))
      .subscribe({
        next: (data: Post) => {
          this.post = data as Post;
          console.log(this.post);
        },
        error: (error: Error) => {
          console.log(error);  	
          this.router.navigate(['/404']);
        }
      })
  }

  deletePost() {
    if(window.confirm("Are you sure you want to delete this post?")) {
      this.postService.Delete(Number(this.route.snapshot.paramMap.get('postId')))
        .subscribe({
          next: () => {
            console.log("Post Deleted");
          },
          error: (error: Error) => {
            console.log(error);
          },
          complete: () => {
            window.alert("Uradio, idi nazad")
          }
        });
    };
  }

}
