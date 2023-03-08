import { Community } from "./community.model";
import { Flair } from "./flair.model";

export class Post {
    postId: number = 0;
    title: string = "";
    text: string = "";
    createdDate: Date = new Date('2022-05-03');
    community: Community = new Community;
    flair: Flair = new Flair;

    Post(postId: number, title: string, text: string, createdDate: Date, community: Community, flair: Flair) {
        this.postId = postId;
        this.title = title;
        this.text = text;
        this.createdDate = createdDate;
        this.community = community;
        this.flair = flair;
    }
}