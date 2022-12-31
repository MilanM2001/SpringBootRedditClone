import { Reaction } from "./reaction.model";

export class User {
    user_id: number;
    username: string;
    password: string;
    email: string;
    avatar: string;
    description: string;
    display_name: string;
    reactions: Reaction[] = [];

    User(user_id: number, username: string, password: string, email: string, avatar: string, description: string, display_name: string, reactions: Reaction[]) {
        user_id = user_id;
        username = username;
        password = password;
        email = email;
        avatar = avatar;
        description = description;
        display_name = display_name;
        reactions = reactions;
    }
}