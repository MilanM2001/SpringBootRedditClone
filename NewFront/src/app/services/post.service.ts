import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Post } from '../model/post.model';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private url = "Post";
  constructor(private http: HttpClient) { }

  public GetAll(): Observable<Post[]> {
    return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/All`);
  }

  public GetSingle(postId: number): Observable<Post> {
    return this.http.get<Post>(`${environment.baseApiUrl}/${this.url}/Single/` + postId);
  }

  public Update(postId: number, post: Post) {
    return this.http.put(`${environment.baseApiUrl}/${this.url}/Update/` + postId, post);
  }

  public Delete(postId: number) {
    return this.http.delete(`${environment.baseApiUrl}/${this.url}/` + postId);
  }
}