import { Flair } from "./flair.model";
import { Post } from "./post.model";
import { Rule } from "./rule.model";

export class Community {
    community_id: number = 0;
    name: string = "";
    description: string = "";
    createdDate: Date = new Date('2022-05-03');
    isSuspended: boolean = false;
    suspendedReason: string = "";
    rules: Rule[] = [];
    flairs: Flair[] = [];

    Community(community_id: number, name: string, description: string, createdDate: Date, isSuspended: boolean, suspendedReason: string, rules: Rule[], flairs: Flair[]) {
        this.community_id = community_id;
        this,name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.isSuspended = isSuspended;
        this.suspendedReason = suspendedReason;
        this.rules = rules;
        this.flairs = flairs;
    }

}