import {Component, OnInit } from '@angular/core';
import {ActivatedRoute, Params, Router} from "@angular/router";
import { AuthService } from "../authentication/auth.service";
import {ItemService} from "../item/item.service";
import {Item, ItemList} from "../item/item";
import {MessagesService} from "../messages/messages.service";
import {HttpParams} from "@angular/common/http";
import {MessagesList} from "../messages/messages";
import {DatePipe} from "@angular/common";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  params: any = {};
  category: string | undefined;
  orderby: string | undefined;
  value_min: number | undefined;
  value_max: number | undefined;
  description: string | undefined;
  location: string | undefined;
  new_messages: number = 0;
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  items: Item[] = [];
  items_photos: any = [];

  pages: number[] = [];
  limit: number = 5;
  page: number = 1;
  count: number = 0;

  searchText: string = '';

  constructor( private router: Router, private datepipe: DatePipe, private messagesService: MessagesService, private authserv: AuthService, private route: ActivatedRoute, private itemserv: ItemService ) { }

  ngOnInit(): void {
    if(this.checkForUser()) {
      this.messagesService.getUnreadMessages()
        .subscribe((response) => {
          this.new_messages = response
        });
    }

    this.route.queryParams
      .subscribe(params => {
          this.params = params;
          console.log(this.params);
          this.page = params['page'];
          this.limit = params['limit'];
          this.category = params['category'];
          this.orderby = params['orderby'];
          this.value_min = params['value_min'];
          this.value_max = params['value_max'];
          this.description = params['description'];
          this.location = params['location'];
        }
      );

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

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  viewItems(): void{
    this.itemserv.getItems(this.params)
      .subscribe((res: any) => {
        console.log(res);
        this.items = res.results;
        for (let i=0; i<this.items.length; i++){
          this.items_photos.push({item: this.items[i], photos: this.itemserv.createPhotos(this.items[i])});
        }
        this.count = res.count;
        this.pages = new Array(Math.ceil(res.count/this.limit)).fill(null).map((_, i) => i + 1);
      });
  }

  thisPage(page_num: number): boolean{
    if (page_num == this.page){
      return true;
    }
    return false;
  }

  newSearch(page: number, limit: number): void{
    if(page <= 0 ){
      page = 1;
    }
    if(this.count != 0 && Math.ceil(this.count/this.limit) < page ){
      page = Math.ceil(this.count/this.limit);
    }

    this.router.navigateByUrl('/', {skipLocationChange: true})
      .then(() => this.router.navigate(['/search'], {
        queryParams: {
          page: page,
          limit: limit,
          category: this.category,
          value_min: this.value_min,
          value_max: this.value_max,
          location: this.location }
      }));
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

  itemsExist(): boolean {
    if (this.pages.length !== 0) {
      return true;
    }
    return false;
  }

  navigate(url: string, category: string){
    if(category === ''){
      this.router.navigateByUrl('/', {skipLocationChange: true})
        .then(() => this.router.navigate([url],
          {queryParams: {page: 1, limit: 10, ends_min: this.now}}));
    } else {
      this.router.navigateByUrl('/', {skipLocationChange: true})
        .then(() => this.router.navigate([url],
          {queryParams: {category: category, page: 1, limit: 10, ends_min: this.now}}));
    }
  }

  searchByDescription(description: string){
    this.router.navigateByUrl('/', {skipLocationChange: true})
        .then(() => this.router.navigate(['/search'],
          {queryParams: {description: description, page: 1, limit: 10, ends_min: this.now}}));
  }

}
