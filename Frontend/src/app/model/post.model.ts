import { Community } from "./community.model";
import { Reaction } from "./reaction.model";
import { User } from "./user.model";

export class Post {
    post_id: number;
    title: string;
    text: string;
    image_path: string;
    reactions: Reaction[] = [];
    community: Community;
    user: User;

    Post(post_id: number, title: string, text: string, image_path: string, reactions: Reaction[], community: Community, user: User) {
        this.post_id = post_id;
        this.title = title;
        this.text = text;
        this.image_path = image_path;
        this.reactions = reactions;
        this.community = community;
        this.user = user;
    }
}