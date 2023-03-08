export class AddCommunityDTO {
    name: string = "";
    description: string = "";

    AddCommunityDTO(name: string, description: string) {
        this.name = name;
        this.description = description;
    }
}