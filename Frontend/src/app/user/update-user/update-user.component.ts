import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/model/user.model';
import { UserService } from 'src/app/service';

@Component({
  selector: 'app-update-user',
  templateUrl: './update-user.component.html',
  styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent implements OnInit {
  formGroup: FormGroup;
  submitted = false;

  user_id: number;
  user: User;

  constructor(private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.user = new User();

    this.formGroup = this.formBuilder.group({
      display_name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(15)]],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(85)]]
    })

    this.user_id = this.route.snapshot.params['user_id'];
    this.userService.getUser(this.user_id)
      .subscribe(data => {
        console.log(data);
        this.user = data;
      }, error => console.log(error));
  }

  get f() {
    return this.formGroup.controls;
  }

  updateUser() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    this.userService.updateUser(this.user_id, this.user).subscribe(
      data => {
        console.log(data);
        this.router.navigateByUrl('/profile/' + this.user_id);
      },
      error => {
        console.log(error);
      });
  }

}
