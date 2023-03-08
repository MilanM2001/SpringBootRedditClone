export class AddPostDTO {
    title: string = "";
    text: string = "";
    flairId: number = 0;

    AddPostDTO(title: string, text: string, flairId: number) {
        this.title = title;
        this.text = text;
        this.flairId = flairId;
    }
}