import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service';

@Component({
  selector: 'app-communities',
  templateUrl: './communities.component.html',
  styleUrls: ['./communities.component.css']
})
export class CommunitiesComponent implements OnInit {

  constructor(private userService: UserService) { }

  ngOnInit() {

  }

  hasSignedIn() {
    return !!this.userService.getCurrent();
  }



}
