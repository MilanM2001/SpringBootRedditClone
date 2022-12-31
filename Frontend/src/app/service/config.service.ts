import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  private _api_url = 'http://localhost:8080/api';

  private _user_url = this._api_url + '/users';
  private _login_url = this._user_url + '/login';
  private _signup_url = this._user_url + '/signup';
  private _whoami_url = this._user_url + '/whoami';

  private _community_url = this._api_url + '/communities'
  private _all_communities_url = this._community_url + '/all'

  get login_url(): string {
    return this._login_url;
  }

  get whoami_url(): string {
    return this._whoami_url;
  }

  get signup_url(): string {
    return this._signup_url;
  }

  get all_communities_url(): string {
    return this._all_communities_url;
  }

}
