import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {User} from "../user";
import {UserService} from "../user.service";
import {MessagesService} from "../../messages/messages.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css'],
  providers: [UserService]
})
export class UserListComponent implements OnInit {

  new_messages: number = 0;
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  users: User[] = [];
  constructor(private messagesService: MessagesService, private datepipe: DatePipe, private fb: FormBuilder, private router: Router, private userService: UserService) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
    .subscribe((response) => {
      this.new_messages = response
    });
    this.getUsers();
  }

  logout(){
    localStorage.removeItem('token');
    localStorage.setItem('isLoggedIn', 'false');
    localStorage.removeItem('username');
    this.router.navigate(['login']);
  }

  getUsers(): void {
    this.userService.getUsers()
      .subscribe((response) => {this.users = response});
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
      'verified': !data.verified
    };

    let r: any = {};
    r = this.userService.updateUser(data.username, userData).subscribe((response: any) => {
      console.log(response);
    });

    console.log(r);
  }

  isVerified(verified: boolean): boolean{
    if (verified === true){
      return true;
    }
    return false;
  }

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

}
