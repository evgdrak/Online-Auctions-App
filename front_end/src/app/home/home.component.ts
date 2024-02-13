import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import {AuthService} from "../authentication/auth.service";
import {MessagesService} from "../messages/messages.service";
import {Item} from "../item/item";
import {ItemService} from "../item/item.service";
import {map} from "rxjs";
import {DatePipe} from "@angular/common";
import exportFromJSON from 'export-from-json'

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  new_messages: number = 0;
  searchText: string = '';
  newItems: Item[] = [];
  endItems: Item[] = [];
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  end_item_photos: any = [];
  new_item_photos: any = [];

  constructor( private router: Router, private datepipe: DatePipe, private authserv: AuthService, private messagesService: MessagesService, private itemserv: ItemService ) { }

  ngOnInit(): void {
    if(this.checkForUser()) {
      this.messagesService.getUnreadMessages()
        .subscribe((response) => {
          this.new_messages = response
        });
    }
    this.viewItems();
  }

  logout(){
    this.authserv.logoutUser();
    this.router.navigate(['login']);
  }

  checkForUser(): boolean{
    if (localStorage.getItem('isLoggedIn') === 'true'){
      return true;
    }
    return false;
  }

  checkForAdmin(): boolean{
    if (localStorage.getItem('username') === 'admin'){
      return true;
    }
    return false;
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  viewItems(): void{
    this.itemserv.getItems({ orderby: '-started', limit: 5, ends_min: this.now})      /* for newest auctions */
      .subscribe((res: any) => {
        console.log(res);

        this.newItems = res.results;      /* res.results : Item[] */

        for (let i=0; i<this.newItems.length; i++){                                             /* create a url for each photo */
          this.new_item_photos.push({item: this.newItems[i], photos: this.itemserv.createPhotos(this.newItems[i])});
        }
        console.log(this.new_item_photos);
      });

    this.itemserv.getItems({ orderby: 'ends', limit: 5, ends_min: this.now})        /* for auctions ending soon */
      .subscribe((res: any) => {
        console.log(res);

        this.endItems = res.results;       /* res.results : Item[] */

        for (let i=0; i<this.endItems.length; i++){                                           /* create a url for each photo */
          this.end_item_photos.push({item: this.endItems[i], photos: this.itemserv.createPhotos(this.endItems[i])});
        }
        console.log(this.end_item_photos);
      });
  }

  redirect(url: string){
    this.router.navigate([url]);
  }

  exportJSON(){         /* for admin */
    let data: any = [];
    this.itemserv.getItems({}).subscribe(res => {
      data = res.results;

      const fileName = 'download';
      const exportType = 'json';

      exportFromJSON({ data, fileName, exportType });
    });

  }

  exportXML(){          /* for admin */
    let data: any = [];
    this.itemserv.getItems({}).subscribe(res => {
      data = res.results;

      const fileName = 'download';
      const exportType = 'xml';

      exportFromJSON({ data, fileName, exportType });
    });

  }

}
