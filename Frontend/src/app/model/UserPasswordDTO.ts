export class UserPasswordDTO {
    public currentPassword: string;
    public password:string;

    constructor() {
        this.currentPassword = "";
        this.password = "";
    }
}