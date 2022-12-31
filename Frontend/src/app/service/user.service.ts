import { Injectable } from '@angular/core';
import { ApiService } from './api.service';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { HttpClient } from '@angular/common/http';
import { UserPasswordDTO } from '../model/UserPasswordDTO';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  api = "http://localhost:8080/api/users/"

  currentUser;

  constructor(
    private apiService: ApiService,
    private config: ConfigService,
    private http: HttpClient
  ) {
  }

  getMyInfo() {
    return this.apiService.get(this.config.whoami_url)
      .pipe(map(user => {
        this.currentUser = user;
        window.localStorage.setItem("user", JSON.stringify(user));
        return user;
      }));
  }

  getCurrent() {

    let userString = window.localStorage.getItem("user");

    if (!userString) {
      return null;
    }

    return JSON.parse(userString);
  }

  getUser(user_id: number): Observable<User> {
    return this.http.get<User>(this.api + user_id);
  }

  getUserPosts(user_id: number) {
    return this.http.get(this.api + user_id + "/posts");
  }

  updateUser(user_id: number, user: User) {
    return this.http.put(this.api + "updateUser" + "/" + user_id, user);
  }

  updatePassword(user_id: number, userDTO: UserPasswordDTO) {
    return this.http.put(this.api + "updatePassword" + "/" + user_id, userDTO);
  }

}
