import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import {MessagesService} from "../messages.service";
import {User} from "../../user/user";
import {MessagesList} from "../messages";
import {HttpParams} from "@angular/common/http";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-register',
  templateUrl: './all-messages.component.html',
  styleUrls: ['./all-messages.component.css']
})
export class AllMessagesComponent implements OnInit {

  messages: MessagesList = new class implements MessagesList {
    count = 0;
    results = [];
  };

  new_messages: number = 0;

  pages: number[] = [];
  limit: number = 5;
  page: number = 1;
  count: number = 0;
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  inbox: boolean = true;

  searchText: string = '';

  constructor( private fb: FormBuilder, private datepipe: DatePipe, private router: Router, private messagesService: MessagesService ) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    this.getInboxMessages(1, 5);
  }

  getInboxMessages(page: number, limit: number): void {
    if(page < 0 ){
      page = 1;
    }
    if(this.count != 0 && Math.ceil(this.count/this.limit) < page ){
      page = Math.ceil(this.count/this.limit);
    }

    let params = new HttpParams().set("send", true);
    params = params.append("page", page);
    params = params.append("limit", limit);
    params = params.append("orderby", "opened");

    this.messagesService.getMessages(params)
      .subscribe((response) => {
          this.messages = response,
          this.inbox = true,
          this.page = page,
          this.count = response.count,
          this.pages = new Array(Math.ceil(response.count/limit)).fill(null).map((_, i) => i + 1);
      });
  }

  getOutboxMessages(page: number, limit: number): void {
    if(page <= 0 ){
      page = 1;
    }
    if(this.count != 0 && Math.ceil(this.count/this.limit) < page ){
      page = Math.ceil(this.count/this.limit);
    }

    let params = new HttpParams().set("send", false);
    params = params.append("page", page);
    params = params.append("limit", limit);

    this.messagesService.getMessages(params)
      .subscribe((response) => {
        this.messages = response,
        this.inbox = false,
        this.page = page,
        this.count = response.count,
        this.pages = new Array(Math.ceil(response.count/limit)).fill(null).map((_, i) => i + 1);
      });
  }

  deleteMessage(id: number): void {
    this.messagesService.deleteMessage(id)
      .subscribe((response) => {location.reload();});
  }

  updateMessage(data: any): void {
    console.log(data);

    let updatedMessage = {
      'title': data.title,
      'body': data.body,
      'timestamp': data.timestamp,
      'sender': data.sender,
      'receiver': data.receiver,
      'opened': true
    };

    let r: any = {};
    r = this.messagesService.updateMessage(updatedMessage, data.id).subscribe((response: any) => {
      this.router.navigate(['/messages/' + data.id]);
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

  thisPage(page_num: number): boolean{
    if (page_num === this.page){
      return true;
    }
    return false;
  }

  isInbox(): boolean{
    return this.inbox;
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
