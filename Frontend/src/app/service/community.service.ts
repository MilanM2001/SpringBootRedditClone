import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Community } from '../model/community.model';
import { Post } from '../model/post.model';

@Injectable({
  providedIn: 'root'
})
export class CommunityService {
  api = "http://localhost:8080/api/communities/"

  constructor(private http: HttpClient) { }

  getAll(): Observable<Array<Community>> {
    return this.http.get<Array<Community>>(this.api + "all");
  }

  addCommunity(community: Community): Observable<Community> {
    return this.http.post<Community>(this.api + "add", community);
  }

  getCommunity(community_id: number): Observable<Community> {
    return this.http.get<Community>(this.api + community_id);
  }

  suspendCommunity(community_id: number, community: Community) {
    return this.http.put(this.api + "suspendCommunity" + "/" + community_id, community);
  }

  updateCommunity(community_id: number, community: Community) {
    return this.http.put(this.api + "updateCommunity" + "/" + community_id, community);
  }

  getCommunityPosts(community_id: number) {
    return this.http.get(this.api + community_id + "/posts")
  }

  getCommunityRules(community_id: number) {
    return this.http.get(this.api + community_id + "/rules")
  }

  getCommunityFlairs(community_id: number) {
    return this.http.get(this.api + community_id + '/flairs')
  }

  addPost(community_id: number, post: Post): Observable<Post> {
    return this.http.post<Post>(this.api + community_id + "/addPost", post);
  }

}
