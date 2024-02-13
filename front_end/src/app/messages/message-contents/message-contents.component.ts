import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import {MessagesService} from "../messages.service";
import {User} from "../../user/user";
import {Messages, MessagesList} from "../messages";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './message-contents.component.html',
  styleUrls: ['./message-contents.component.css']
})
export class MessageContentsComponent implements OnInit {

  new_messages: number = 0;

  message: Messages = new class implements Messages {
    body = '';
    id = 0;
    opened = false;
    receiver = '';
    sender = '';
    timestamp = '';
    title = '';
  };
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  searchText: string = '';

  constructor( private fb: FormBuilder, private datepipe: DatePipe, private activatedRoute: ActivatedRoute, private router: Router, private messagesService: MessagesService ) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    const id = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    this.getMessage(id);
  }

  getMessage(id: number): void {
    this.messagesService.getMessage(id)
      .subscribe((response) => {this.message = response});
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

  reply(sender: string, title: string){
    localStorage.setItem('reply', sender);
    localStorage.setItem('title', title);
    this.router.navigate(['/messages']);
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

}
