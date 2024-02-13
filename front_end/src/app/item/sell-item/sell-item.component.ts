import { Component, OnInit } from '@angular/core';
import { AuthService } from "../../authentication/auth.service";
import { ActivatedRoute, Router } from "@angular/router";
import {FormArray, FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import { DatePipe } from "@angular/common";
import { map } from "rxjs";
import { Item } from "../item";
import { UserService } from "../../user/user.service";
import { ItemService } from "../item.service";
import * as L from 'leaflet';
import {LayerGroup} from "leaflet";
import {MessagesService} from "../../messages/messages.service";
import {DomSanitizer} from "@angular/platform-browser";
import {HttpParams} from "@angular/common/http";

@Component({
  selector: 'app-sell-item',
  templateUrl: './sell-item.component.html',
  styleUrls: ['./sell-item.component.css']
})
export class SellItemComponent implements OnInit {

  itemForm: any = FormGroup;
  userInfo: any = {};
  sell: boolean = true;
  items: Item[] = [];
  items_photos: any = [];
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  new_messages: number = 0;
  pages: number[] = [];
  limit: number = 10;
  page: number = 1;
  count: number = 0;

  map!: L.Map | LayerGroup<any>;

  xlng : number = 0;
  xlat : number = 0;
  marker_count : number = 0;
  marker!: L.Marker<any>;

  constructor( private fb: FormBuilder, private messagesService: MessagesService, private authserv: AuthService, private itemserv: ItemService, private router: Router,
               private activatedRoute: ActivatedRoute, private userserv: UserService, private datepipe: DatePipe, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});

    this.itemForm = this.fb.group({
      name: ['',Validators.required],
      categories: this.fb.array([this.fb.group({"name": ''}, Validators.required)]),
      location: ['',Validators.required],
      latitude: [''],
      longitude: [''],
      country: ['',Validators.required],
      buy_price: ['',Validators.required],
      first_bid: ['',Validators.required],
      ends: ['',Validators.required],
      rating: ['',Validators.required],
      description: ['',Validators.required],
      photos: this.fb.array([this.fb.group({"img": '', "url": ''})])
    });
    this.getUserInfo();
  }

  private initMap(): void {
    this.map = L.map('map', {
      center: [ 37.968345, 23.767497 ],
      zoom: 9
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);

    this.map.on('click', (e) => {
      if(this.marker_count === 0) {
        this.xlng = e.latlng.lng;
        this.xlat = e.latlng.lat;
        var myIcon = L.icon({
          iconUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAApCAYAAADAk4LOAAAFgUlEQVR4Aa1XA5BjWRTN2oW17d3YaZtr2962HUzbDNpjszW24mRt28p47v7zq/bXZtrp/lWnXr337j3nPCe85NcypgSFdugCpW5YoDAMRaIMqRi6aKq5E3YqDQO3qAwjVWrD8Ncq/RBpykd8oZUb/kaJutow8r1aP9II0WmLKLIsJyv1w/kqw9Ch2MYdB++12Onxee/QMwvf4/Dk/Lfp/i4nxTXtOoQ4pW5Aj7wpici1A9erdAN2OH64x8OSP9j3Ft3b7aWkTg/Fm91siTra0f9on5sQr9INejH6CUUUpavjFNq1B+Oadhxmnfa8RfEmN8VNAsQhPqF55xHkMzz3jSmChWU6f7/XZKNH+9+hBLOHYozuKQPxyMPUKkrX/K0uWnfFaJGS1QPRtZsOPtr3NsW0uyh6NNCOkU3Yz+bXbT3I8G3xE5EXLXtCXbbqwCO9zPQYPRTZ5vIDXD7U+w7rFDEoUUf7ibHIR4y6bLVPXrz8JVZEql13trxwue/uDivd3fkWRbS6/IA2bID4uk0UpF1N8qLlbBlXs4Ee7HLTfV1j54APvODnSfOWBqtKVvjgLKzF5YdEk5ewRkGlK0i33Eofffc7HT56jD7/6U+qH3Cx7SBLNntH5YIPvODnyfIXZYRVDPqgHtLs5ABHD3YzLuespb7t79FY34DjMwrVrcTuwlT55YMPvOBnRrJ4VXTdNnYug5ucHLBjEpt30701A3Ts+HEa73u6dT3FNWwflY86eMHPk+Yu+i6pzUpRrW7SNDg5JHR4KapmM5Wv2E8Tfcb1HoqqHMHU+uWDD7zg54mz5/2BSnizi9T1Dg4QQXLToGNCkb6tb1NU+QAlGr1++eADrzhn/u8Q2YZhQVlZ5+CAOtqfbhmaUCS1ezNFVm2imDbPmPng5wmz+gwh+oHDce0eUtQ6OGDIyR0uUhUsoO3vfDmmgOezH0mZN59x7MBi++WDL1g/eEiU3avlidO671bkLfwbw5XV2P8Pzo0ydy4t2/0eu33xYSOMOD8hTf4CrBtGMSoXfPLchX+J0ruSePw3LZeK0juPJbYzrhkH0io7B3k164hiGvawhOKMLkrQLyVpZg8rHFW7E2uHOL888IBPlNZ1FPzstSJM694fWr6RwpvcJK60+0HCILTBzZLFNdtAzJaohze60T8qBzyh5ZuOg5e7uwQppofEmf2++DYvmySqGBuKaicF1blQjhuHdvCIMvp8whTTfZzI7RldpwtSzL+F1+wkdZ2TBOW2gIF88PBTzD/gpeREAMEbxnJcaJHNHrpzji0gQCS6hdkEeYt9DF/2qPcEC8RM28Hwmr3sdNyht00byAut2k3gufWNtgtOEOFGUwcXWNDbdNbpgBGxEvKkOQsxivJx33iow0Vw5S6SVTrpVq11ysA2Rp7gTfPfktc6zhtXBBC+adRLshf6sG2RfHPZ5EAc4sVZ83yCN00Fk/4kggu40ZTvIEm5g24qtU4KjBrx/BTTH8ifVASAG7gKrnWxJDcU7x8X6Ecczhm3o6YicvsLXWfh3Ch1W0k8x0nXF+0fFxgt4phz8QvypiwCCFKMqXCnqXExjq10beH+UUA7+nG6mdG/Pu0f3LgFcGrl2s0kNNjpmoJ9o4B29CMO8dMT4Q5ox8uitF6fqsrJOr8qnwNbRzv6hSnG5wP+64C7h9lp30hKNtKdWjtdkbuPA19nJ7Tz3zR/ibgARbhb4AlhavcBebmTHcFl2fvYEnW0ox9xMxKBS8btJ+KiEbq9zA4RthQXDhPa0T9TEe69gWupwc6uBUphquXgf+/FrIjweHQS4/pduMe5ERUMHUd9xv8ZR98CxkS4F2n3EUrUZ10EYNw7BWm9x1GiPssi3GgiGRDKWRYZfXlON+dfNbM+GgIwYdwAAAAASUVORK5CYII='
        })
        this.marker = L.marker(e.latlng, {icon: myIcon, draggable: true}).addTo(this.map);
        this.marker_count = 1;

        this.marker.on('dragend', () => {
          this.xlng = this.marker.getLatLng().lat;
          this.xlat = this.marker.getLatLng().lng;
        });
      }
    });

  }

  ngAfterViewInit() {
    this.initMap();
  }

  getUserName(): string | null{
    return localStorage.getItem('username');
  }

  getUserInfo(): void{
    const uname = String(localStorage.getItem('username'));
    this.userserv.getUser(uname).subscribe((res) => { console.log(res);
      this.userInfo = res;});
  }


  logout(){
    this.authserv.logoutUser();
    this.router.navigate(['login']);
  }

  changeVisibility(): void{
    location.reload();
    this.sell = true;
  }

  get categories(): FormArray{
    return this.itemForm.controls["categories"] as FormArray;
  }

  addCategory(){
    this.categories.push(this.fb.group({"name": ''}));
  }

  removeCategory(i: number){
    this.categories.removeAt(i);
  }

  get photos(): FormArray{
    return this.itemForm.controls["photos"] as FormArray;
  }

  addPhoto(){
    this.photos.push(this.fb.group({"img": '', "url": ''}));
  }

  removePhoto(i: number){
    this.photos.removeAt(i);
  }

  addItem(data: any){

    let date = new Date();
    let start_date = this.datepipe.transform(date, 'yyyy-MM-dd hh:mm');

    let itemData = {
      'name': data.name,
      'categories': data.categories,
      'buy_price': data.buy_price,
      'currently': data.first_bid,
      'first_bid': data.first_bid,
      'number_of_bids': 0,
      'location': data.location,
      'latitude': this.xlat,
      'longitude': this.xlng,
      'country': data.country,
      'started': start_date,
      'ends': data.ends,
      'description': data.description,
      'rating': data.rating,
      'userID': this.userInfo?.username,
      'photos': data.photos
    }

    console.log(itemData);

    const formData = new FormData();        //convert data to be sent in the appropriate form
    formData.append('item', new Blob([JSON.stringify(itemData)], {type: 'application/json'}));

    for (let i=0; i<data.photos.length; i++){
      formData.append('photo', data.photos[i].img, data.photos[i].img.name);
    }

    this.itemserv.addItem(formData).subscribe((res: any) => {console.log(res);    //make request for new item
      if (res) {
        alert("Item is up for sale!");
        location.reload();
      }});
  }


  viewItems(page: number, limit: number): void{
    if(page <= 0 ){
      page = 1;
    }
    if(this.count != 0){
      if(this.count > this.limit) {
        if (Math.ceil(this.count / this.limit) < page) {
          page = Math.ceil(this.count / this.limit);
        }
      } else {
        page = 1;
      }
    }

    let params = new HttpParams();
    params = params.append("page", page);
    params = params.append("limit", limit);
    // @ts-ignore
    params = params.append("user", localStorage.getItem('username'));

    this.sell = false;
    this.itemserv.getItems(params)
      .subscribe((res: any) => {
        this.items_photos = [];
        console.log(res);

        this.items = res.results;
        for (let i=0; i<this.items.length; i++){
          this.items_photos.push({item: this.items[i], photos: this.itemserv.createPhotos(this.items[i])});
        }
        this.page = page,
          this.count = res.count,
          this.limit = limit;
        this.pagesFill(res.count, limit)

      });
  }

  pagesFill(count: number, limit: number){
    if(count > limit) {
      this.pages = new Array(Math.ceil(count/limit)).fill(null).map((_, i) => i + 1);
    } else {
      console.log(count);
      console.log(limit);
      this.pages = [1];
    }
  }

  deleteItem(id: number): void{
    this.itemserv.deleteItem(id).subscribe(res => console.log(res));
    location.reload();
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

  onFileSelected(event: any, i: number){
    if (event.target.files){
      const file = event.target.files[0];
      console.log(file);

      let img = file;
      let url = this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(file));
      console.log(url);

      this.photos.at(i).setValue({'img': img, 'url': url});
      console.log(this.photos.value)

    }
  }

  thisPage(page_num: number): boolean{
    if (page_num === this.page){
      return true;
    }
    return false;
  }

}
