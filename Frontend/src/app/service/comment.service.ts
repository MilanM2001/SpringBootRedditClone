import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Comment } from "../model/comment.model";

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  api = "http://localhost:8080/api/comments/"

  constructor(private http: HttpClient) { }

  getAll(): Observable<Array<Comment>> {
    return this.http.get<Array<Comment>>(this.api + "all");
  }

  getComment(comment_id: number): Observable<Comment> {
    return this.http.get<Comment>(this.api + comment_id);
  }

  updateComment(comment_id: number, comment: Comment) {
    return this.http.put(this.api + "updateComment" + "/" + comment_id, comment);
  }

  delete(comment_id: number) {
    return this.http.delete(this.api + comment_id);
  }

}