import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { LoginDTO } from '../dto/loginDTO';
import { RegisterDTO } from '../dto/registerDTO';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url = "users";
  constructor(private http: HttpClient) { }

  public register(registerDTO: RegisterDTO): Observable<User> {
    return this.http.post<User>(`${environment.baseApiUrl}/${this.url}/register`, registerDTO);
  }

  public login(loginDTO: LoginDTO): Observable<string> {
    return this.http.post(`${environment.baseApiUrl}/${this.url}/login`, loginDTO, { responseType: 'text' });
  }

  public GetMe(): Observable<User> {
    return this.http.get<User>(`${environment.baseApiUrl}/${this.url}/GetMe`);
}

  // public getMe(): Observable<string> {
  //   return this.http.get<string>(`${environment.baseApiUrl}/${this.url}/GetMe`);
  // }

}