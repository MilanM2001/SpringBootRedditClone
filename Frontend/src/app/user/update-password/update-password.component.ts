import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from 'src/app/model/user.model';
import { UserPasswordDTO } from 'src/app/model/UserPasswordDTO';
import { UserService } from 'src/app/service';

@Component({
  selector: 'app-update-password',
  templateUrl: './update-password.component.html',
  styleUrls: ['./update-password.component.css']
})
export class UpdatePasswordComponent implements OnInit {
  submitted = false;

  user_id: number;
  user: User;

  error:boolean = false;

  formGroup = this.fb.group({
    currentPassword: ["", [
      Validators.required
    ]],
    password: ["", [
      Validators.required,
      Validators.minLength(3)
    ]]
  });

  constructor(private userService: UserService,
    private route: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder) { }

  ngOnInit() {
    this.user = new User();

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

  updatePassword() {
    this.submitted = true;

    if (this.formGroup.invalid) {
      return;
    }

    let userDTO = new UserPasswordDTO();
     userDTO.currentPassword = this.formGroup.value.currentPassword;
     userDTO.password = this.formGroup.value.password;

    this.userService.updatePassword(this.user_id, userDTO).subscribe(
      data => {
        console.log(data);
        this.router.navigateByUrl('/profile/' + this.user_id);
      },
      error => {
        console.log("neuspesno");
        this.error = true;
      });
  }

}
