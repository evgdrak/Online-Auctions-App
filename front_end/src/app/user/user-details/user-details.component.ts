import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../user";
import {MessagesService} from "../../messages/messages.service";
import {UserService} from "../user.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  new_messages: number = 0;
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  user: User = new class implements User {
    id = 0;
    username = '';
    firstName = '';
    lastName = '';
    email = '';
    phone = '';
    address = '';
    city = '';
    country = '';
    tin = '';
    role = 1;
    verified = false;
  }

  userForm: any = FormGroup;

  constructor(private messagesService: MessagesService, private datepipe: DatePipe, private fb: FormBuilder, private activatedRoute: ActivatedRoute, private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    const uname = String(this.activatedRoute.snapshot.paramMap.get('username'));
    this.userForm = this.fb.group({
      username: ['',Validators.required],
      firstName: ['',Validators.required],
      lastName: ['',Validators.required],
      email: ['',Validators.required],
      phone: ['',Validators.required],
      address: ['',Validators.required],
      city: ['',Validators.required],
      country: ['',Validators.required],
      tin: ['',Validators.required],
      role: ['',Validators.required]});
    this.getUser(uname);
  }

  logout(){
    localStorage.removeItem('token');
    localStorage.setItem('isLoggedIn', 'false');
    localStorage.removeItem('username');
    this.router.navigate(['login']);
  }

  checkForAdmin(): boolean{
    if (localStorage.getItem('username') === 'admin'){
      return true;
    }
    return false;
  }

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  getUser(uname: string) {
    this.userService.getUser(uname).subscribe((response) => {
      this.user = response;

      this.userForm = this.fb.group({
        username: [response.username,Validators.required],
        firstName: [response.firstName,Validators.required],
        lastName: [response.lastName,Validators.required],
        email: [response.email,Validators.required],
        phone: [response.phone,Validators.required],
        address: [response.address,Validators.required],
        city: [response.city,Validators.required],
        country: [response.country,Validators.required],
        tin: [response.tin,Validators.required],
        role: [response.role,Validators.required]});
    })
  }

  updateUser(data: User) {
    console.log(data);

    let userData = {
      'id': data.id,
      'username': data.username,
      'firstName': data.firstName,
      'lastName': data.lastName,
      'email': data.email,
      'phone': data.phone,
      'address': data.address,
      'city': data.city,
      'country': data.country,
      'tin': data.tin,
      'role': data.role,
      'verified': this.user.verified
    };

    let r: any = {};
    r = this.userService.updateUser(this.user.username, userData).subscribe((response: any) => {
      return localStorage.setItem('username', data.username);
    });

    console.log(r);
  }

  redirect(url: string){
    this.router.navigate([url]);
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

}
