export class RegisterDTO {
    username: string = "";
    password: string = "";
    email: string = "";
    displayName: string = "";

    RegisterDTO(username: string, password: string, email: string, displayName: string) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.displayName = displayName;
    }
}