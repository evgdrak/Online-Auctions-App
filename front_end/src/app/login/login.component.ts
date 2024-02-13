import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthService } from "../authentication/auth.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  login: any = FormGroup;

  constructor( private fb: FormBuilder, private router: Router, private authserv: AuthService ) { }

  ngOnInit(): void {
    this.login = this.fb.group({
      username: ['',Validators.required],
      password: ['',Validators.required]});
  }

  loginSubmit(data: any){
    let userData = {
      'username': data.username,
      'password': data.password
    }

    this.authserv.loginUser(userData).subscribe(res =>
    {localStorage.setItem('token', res.token);
      localStorage.setItem('isLoggedIn', 'true');
      localStorage.setItem('username', data.username);
      if (data.username === 'admin'){               //for admin, redirect to list of users
        this.router.navigate(['users']);
      } else {
        this.router.navigate(['home']);   //for regular users, redirect to home page
      }
    });
  }

}
