import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../authentication/auth.service";
import {Router} from "@angular/router";
import {ItemService} from "../item.service";
import {Item} from "../item";
import {map} from "rxjs";
import {UserService} from "../../user/user.service";
import {MessagesService} from "../../messages/messages.service";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-bid-item',
  templateUrl: './bid-item.component.html',
  styleUrls: ['./bid-item.component.css']
})
export class BidItemComponent implements OnInit {

  items: Item[] = [];
  items_photos: any = [];
  userInfo: any = {};
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  new_messages: number = 0;

  constructor( private authserv: AuthService, private datepipe: DatePipe, private messagesService: MessagesService, private userserv: UserService, private router: Router, private itemserv: ItemService) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    this.getUserInfo();
    this.viewItems();
  }


  logout(){
    this.authserv.logoutUser();
    this.router.navigate(['login']);
  }

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  getUserInfo(): void{
    const uname = String(localStorage.getItem('username'));
    this.userserv.getUser(uname).subscribe((res) => { console.log(res);
      this.userInfo = res;});
  }


  viewItems(): void{
    // @ts-ignore
    this.itemserv.getItemsWithBidsFromUser(localStorage.getItem('username'))    //get items where current user has made a bid
      .subscribe((res: any) => {
        console.log(res);

        this.items = res;
        console.log(this.items);

        for (let i=0; i<this.items.length; i++){      //create url for photos
          this.items_photos.push({item: this.items[i], photos: this.itemserv.createPhotos(this.items[i])});
        }
      });
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

}
