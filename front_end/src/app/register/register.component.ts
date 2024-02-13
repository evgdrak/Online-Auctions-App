import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../authentication/auth.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  register: any = FormGroup;

  constructor( private fb: FormBuilder, private router: Router, private authserv: AuthService ) { }

  ngOnInit(): void {
    this.register = this.fb.group({
      username: ['',Validators.required],
      password: ['',Validators.required],
      firstName: ['',Validators.required],
      lastName: ['',Validators.required],
      email: ['',Validators.required],
      phone: ['',Validators.required],
      address: ['',Validators.required],
      city: ['',Validators.required],
      country: ['',Validators.required],
      tin: ['',Validators.required]});
  }

  registerNewUser(data: any): void {
    console.log(data);

    let userData = {
      'username': data.username,
      'password': data.password,
      'firstName': data.firstName,
      'lastName': data.lastName,
      'email': data.email,
      'phone': data.phone,
      'address': data.address,
      'city': data.city,
      'country': data.country,
      'tin': data.tin,
      'role': 'User'
    };

    let r: any = {};
    r = this.authserv.registerUser(userData).subscribe((response: any) => {
      console.log(response);
      if (response.status === 200) {
        localStorage.setItem('isRegistered', 'true');
        this.router.navigate(['request']);      //redirect to page informing about registration
      } else {
        localStorage.clear();
      }
    });

    console.log(r);

  }

}
