import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { AddPostDTO } from "../dto/addPostDTO";
import { Community } from "../model/community.model";
import { Flair } from "../model/flair.model";
import { Post } from "../model/post.model";
import { Rule } from "../model/rule.model";

@Injectable({
    providedIn: 'root'
})
export class CommunityService {
    private url = "Community";
    constructor(private http: HttpClient) { }

    public GetAll(): Observable<Community[]> {
        return this.http.get<Community[]>(`${environment.baseApiUrl}/${this.url}/All`);
    }

    public GetSingle(communityId: number): Observable<Community> {
        return this.http.get<Community>(`${environment.baseApiUrl}/${this.url}/Single/` + communityId);
    }

    public GetCommunityPosts(communityId: number): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.baseApiUrl}/${this.url}/Posts/` + communityId);
    }

    public GetCommunityFlairs(communityId: number): Observable<Flair[]> {
        return this.http.get<Flair[]>(`${environment.baseApiUrl}/${this.url}/Flairs/` + communityId);
    }

    public GetCommunityRules(communityId: number): Observable<Rule[]> {
        return this.http.get<Rule[]>(`${environment.baseApiUrl}/${this.url}/Rules/` + communityId);
    }

    public AddCommunity(addCommunityDTO: Community): Observable<Community> {
        return this.http.post<Community>(`${environment.baseApiUrl}/${this.url}/Add`, addCommunityDTO);
      }

    public AddPost(communityId: number, post: AddPostDTO): Observable<Post> {
        return this.http.post<Post>(`${environment.baseApiUrl}/${this.url}/AddPost/` + communityId, post);
    }

    public AddRule(communityId: number, community: Community) {
        return this.http.put(`${environment.baseApiUrl}/${this.url}/AddRule/` + communityId, community);
    }

    public Update(communityId: number, community: Community) {
        return this.http.put(`${environment.baseApiUrl}/${this.url}/Update/` + communityId, community);
    }
}