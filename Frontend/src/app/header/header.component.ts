import { Component, OnInit } from '@angular/core';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private userService: UserService) { }

  ngOnInit() {
  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }

  userName() {
    const user = this.userService.getCurrent();
    return user.username;
  }

  role() {
    const user = this.userService.getCurrent();
    return user.role;
  }

  isUser() {
    return this.role() === 'USER';
  }

  isAdmin() {
    return this.role() === 'ADMIN';
  }
}
