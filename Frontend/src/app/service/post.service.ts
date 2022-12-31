import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Post } from '../model/post.model';
import { Observable } from 'rxjs';
import { Comment } from '../model/comment.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  api = "http://localhost:8080/api/posts/"

  constructor(private http: HttpClient) { }

  getAll(): Observable<Array<Post>> {
    return this.http.get<Array<Post>>(this.api + "all");
  }

  getPost(post_id: number): Observable<Post> {
    return this.http.get<Post>(this.api + post_id);
  }

  updatePost(post_id: number, post: Post) {
    return this.http.put(this.api + "updatePost" + "/" + post_id, post);
  }

  getPostComments(post_id: number) {
    return this.http.get(this.api + post_id + "/comments")
  }

  addComment(post_id: number, comment: Comment): Observable<Comment> {
    return this.http.post<Comment>(this.api + post_id + "/addComment", comment);
  }

  delete(post_id: number) {
    return this.http.delete(this.api + post_id);
  }

}