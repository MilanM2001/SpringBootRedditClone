import { Post } from "./post.model";
import { Rule } from "./rule.model";

export class Community {
    communityId: number = 0;
    name: string = "";
    description: string = "";
    createdDate: Date = new Date('2022-05-03');
    isSuspended: boolean = false;
    suspendedReason: string = "";
    posts: Post[] = [];
    rules: Rule[] = [];

    Community(communityId: number, name: string, description: string, createdDate: Date, isSuspended: boolean, suspendedReason: string, posts: Post[], rules: Rule[]) {
        this.communityId = communityId;
        this,name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.isSuspended = isSuspended;
        this.suspendedReason = suspendedReason;
        this.posts = posts;
        this.rules = rules;
    }

}