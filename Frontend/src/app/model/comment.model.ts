import { Reaction } from "./reaction.model";
import { User } from "./user.model";

export class Comment {
    comment_id: number;
    text: string;
    user: User;
    reactions: Reaction[] = [];

    Comment(comment_id: number, text: string, user: User, reactions: Reaction[]) {
        this.comment_id = comment_id;
        this.text = text;
        this.user = user;
        this.reactions = reactions;
    }
}