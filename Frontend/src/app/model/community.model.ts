import { Moderator } from "./moderator.model";

export class Community {
    community_id: number
    name: string
    description: string
    posts: any;
    rules: any;
    flairs: any;
    is_suspended: boolean
    suspended_reason: string
    moderators: Moderator[] = [];

    Community(community_id: number, name: string, description: string, posts: any, is_suspended: boolean, suspended_reason: string, flairs: any, rules: any, moderators: Moderator[]) {
        this.community_id = community_id;
        this.name = name;
        this.description = description;
        this.posts = posts;
        this.is_suspended = is_suspended;
        this.suspended_reason = suspended_reason;
        this.rules = rules;
        this.flairs = flairs;
        this.moderators = moderators;
    }
}