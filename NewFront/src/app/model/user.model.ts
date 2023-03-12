export class User {
    user_id: number = 0;
    username: string = "";
    password: string = "";
    avatar: string = "";
    email: string = "";
    dateOfRegistration: Date = new Date('2022-05-03');
    description: string = "";
    displayName: string = "";

    User(user_id: number, username: string, password: string, avatar: string, email: string,  dateOfRegistration: Date, description: string, displayName: string) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.avatar = avatar;
        this.email = email;
        this.dateOfRegistration = dateOfRegistration;
        this.description = description;
        this.displayName = displayName;
    }
}