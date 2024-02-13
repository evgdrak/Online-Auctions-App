import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from "../../authentication/auth.service";
import {MessagesService} from "../messages.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './new-message.component.html',
  styleUrls: ['./new-message.component.css']
})
export class NewMessageComponent implements OnInit {

  new_messages: number = 0;
  message: any = FormGroup;
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  constructor( private fb: FormBuilder, private router: Router, private messagesService: MessagesService, private datepipe: DatePipe) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    let receiver : string = '';
    let title : string = '';

    if (localStorage.getItem('reply') !== null){
      receiver = String(localStorage.getItem('reply'));
      localStorage.removeItem('reply')
    }

    if (localStorage.getItem('title') !== null){
      title = 'Re:' + String(localStorage.getItem('title'));
      localStorage.removeItem('title')
    }

    this.message = this.fb.group({
      title: [title,Validators.required],
      body: ['',Validators.required],
      timestamp: ['',Validators.required],
      sender: [this.getUserName(),Validators.required],
      receiver: [receiver,Validators.required],
      opened: ['',Validators.required]});
  }

  addMessage(data: any): void {
    let date = new Date();
    let timestamp =this.datepipe.transform(date, 'yyyy-MM-dd hh:mm');

    let newMessage = {
      'title': data.title,
      'body': data.body,
      'timestamp': timestamp,
      'sender': data.sender,
      'receiver': data.receiver,
      'opened': false
    };

    let r: any = {};
    r = this.messagesService.addMessage(newMessage).subscribe((response: any) => {
      this.router.navigate(['/messages/user/' + this.getUserName()]);
    });

    console.log(r);

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
