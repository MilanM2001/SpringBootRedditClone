import { Token } from '@angular/compiler';
import { tokenize } from '@angular/compiler/src/ml_parser/lexer';
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-user-menu',
  templateUrl: './user-menu.component.html',
  styleUrls: ['./user-menu.component.css']
})
export class UserMenuComponent implements OnInit {

  user: any;

  constructor(
    private authService: AuthService,
    private userService: UserService) { }

  ngOnInit() {
    this.user = this.userService.currentUser;
  }

  logout() {
    this.authService.logout();
    localStorage.clear();
  }

  user_id() {
    const user = this.userService.getCurrent();
    return user.user_id;
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

}
