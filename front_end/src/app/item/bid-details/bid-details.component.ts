import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { AuthService } from "../../authentication/auth.service";
import { UserService } from "../../user/user.service";
import { ItemService } from "../item.service";
import {DatePipe, Location} from "@angular/common";
import {MessagesService} from "../../messages/messages.service";
import * as L from "leaflet";
import {LayerGroup} from "leaflet";

@Component({
  selector: 'app-bid-details',
  templateUrl: './bid-details.component.html',
  styleUrls: ['./bid-details.component.css']
})
export class BidDetailsComponent implements OnInit {

  itemInfo: any = {};
  userInfo: any = {};
  guest: boolean = true;
  bid: boolean = false;
  confirm: boolean = false;
  bidForm: any = FormGroup;
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  new_messages: number = 0;

  map!: L.Map | LayerGroup<any>;

  xlng : number = 0;
  xlat : number = 0;
  loc : string = '';
  marker_count : number = 0;
  marker!: L.Marker<any>;

  photos: any = [];

  constructor( private location: Location, private authserv: AuthService, private messagesService: MessagesService, private userserv: UserService, private itemserv: ItemService, private route: ActivatedRoute, private router: Router, private fb: FormBuilder, private datepipe: DatePipe) { }

  ngOnInit(): void {
    if(this.checkForUser()) {
      this.guest = false;
      this.messagesService.getUnreadMessages()
        .subscribe((response) => {
          this.new_messages = response
        });

      this.bidForm = this.fb.group({      //initialize bid form
        amount: ['',Validators.required],
        rating: ['',Validators.required]
      });

      this.getUserInfo();

    }

    this.viewItem();
  }

  private initMap(): void {
    console.log(this.xlat);
    this.map = L.map('map', {
      center: [ this.xlat, this.xlng ],
      zoom: 9
    });

    const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      maxZoom: 18,
      minZoom: 3,
      attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    });

    tiles.addTo(this.map);
    var myIcon = L.icon({
      iconUrl: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAApCAYAAADAk4LOAAAFgUlEQVR4Aa1XA5BjWRTN2oW17d3YaZtr2962HUzbDNpjszW24mRt28p47v7zq/bXZtrp/lWnXr337j3nPCe85NcypgSFdugCpW5YoDAMRaIMqRi6aKq5E3YqDQO3qAwjVWrD8Ncq/RBpykd8oZUb/kaJutow8r1aP9II0WmLKLIsJyv1w/kqw9Ch2MYdB++12Onxee/QMwvf4/Dk/Lfp/i4nxTXtOoQ4pW5Aj7wpici1A9erdAN2OH64x8OSP9j3Ft3b7aWkTg/Fm91siTra0f9on5sQr9INejH6CUUUpavjFNq1B+Oadhxmnfa8RfEmN8VNAsQhPqF55xHkMzz3jSmChWU6f7/XZKNH+9+hBLOHYozuKQPxyMPUKkrX/K0uWnfFaJGS1QPRtZsOPtr3NsW0uyh6NNCOkU3Yz+bXbT3I8G3xE5EXLXtCXbbqwCO9zPQYPRTZ5vIDXD7U+w7rFDEoUUf7ibHIR4y6bLVPXrz8JVZEql13trxwue/uDivd3fkWRbS6/IA2bID4uk0UpF1N8qLlbBlXs4Ee7HLTfV1j54APvODnSfOWBqtKVvjgLKzF5YdEk5ewRkGlK0i33Eofffc7HT56jD7/6U+qH3Cx7SBLNntH5YIPvODnyfIXZYRVDPqgHtLs5ABHD3YzLuespb7t79FY34DjMwrVrcTuwlT55YMPvOBnRrJ4VXTdNnYug5ucHLBjEpt30701A3Ts+HEa73u6dT3FNWwflY86eMHPk+Yu+i6pzUpRrW7SNDg5JHR4KapmM5Wv2E8Tfcb1HoqqHMHU+uWDD7zg54mz5/2BSnizi9T1Dg4QQXLToGNCkb6tb1NU+QAlGr1++eADrzhn/u8Q2YZhQVlZ5+CAOtqfbhmaUCS1ezNFVm2imDbPmPng5wmz+gwh+oHDce0eUtQ6OGDIyR0uUhUsoO3vfDmmgOezH0mZN59x7MBi++WDL1g/eEiU3avlidO671bkLfwbw5XV2P8Pzo0ydy4t2/0eu33xYSOMOD8hTf4CrBtGMSoXfPLchX+J0ruSePw3LZeK0juPJbYzrhkH0io7B3k164hiGvawhOKMLkrQLyVpZg8rHFW7E2uHOL888IBPlNZ1FPzstSJM694fWr6RwpvcJK60+0HCILTBzZLFNdtAzJaohze60T8qBzyh5ZuOg5e7uwQppofEmf2++DYvmySqGBuKaicF1blQjhuHdvCIMvp8whTTfZzI7RldpwtSzL+F1+wkdZ2TBOW2gIF88PBTzD/gpeREAMEbxnJcaJHNHrpzji0gQCS6hdkEeYt9DF/2qPcEC8RM28Hwmr3sdNyht00byAut2k3gufWNtgtOEOFGUwcXWNDbdNbpgBGxEvKkOQsxivJx33iow0Vw5S6SVTrpVq11ysA2Rp7gTfPfktc6zhtXBBC+adRLshf6sG2RfHPZ5EAc4sVZ83yCN00Fk/4kggu40ZTvIEm5g24qtU4KjBrx/BTTH8ifVASAG7gKrnWxJDcU7x8X6Ecczhm3o6YicvsLXWfh3Ch1W0k8x0nXF+0fFxgt4phz8QvypiwCCFKMqXCnqXExjq10beH+UUA7+nG6mdG/Pu0f3LgFcGrl2s0kNNjpmoJ9o4B29CMO8dMT4Q5ox8uitF6fqsrJOr8qnwNbRzv6hSnG5wP+64C7h9lp30hKNtKdWjtdkbuPA19nJ7Tz3zR/ibgARbhb4AlhavcBebmTHcFl2fvYEnW0ox9xMxKBS8btJ+KiEbq9zA4RthQXDhPa0T9TEe69gWupwc6uBUphquXgf+/FrIjweHQS4/pduMe5ERUMHUd9xv8ZR98CxkS4F2n3EUrUZ10EYNw7BWm9x1GiPssi3GgiGRDKWRYZfXlON+dfNbM+GgIwYdwAAAAASUVORK5CYII='
    })
    this.marker = L.marker([ this.xlat, this.xlng ], {icon: myIcon, draggable: false}).addTo(this.map);
  }

  back(): void {
    this.location.back();
  }

  checkForUser(): boolean{
    if (localStorage.getItem('isLoggedIn') === 'true'){
      return true;
    } else {
      return false;
    }
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

  getUserInfo(): void{
    const uname = String(localStorage.getItem('username'));
    this.userserv.getUser(uname).subscribe((res) => { console.log(res);
      this.userInfo = res;});
  }

  logout(){
    this.authserv.logoutUser();
    this.router.navigate(['login']);
  }

  viewItem(): void{
    const id = this.route.snapshot.paramMap.get('id');
    this.itemserv.getItem(Number(id))
      .subscribe((res: any) => {
        console.log(res);
        this.itemInfo = res;
        this.xlat = res.latitude;
        this.xlng = res.longitude;
        this.loc = res.location;
        this.initMap();

        this.photos = this.itemserv.createPhotos(this.itemInfo);    //create a url for each photo
      });
  }

  isOpenAuction(): boolean {
    let currentDate = new Date();
    let endDate = new Date(this.itemInfo.ends);
    if (currentDate.getTime() < endDate.getTime()){     //auction has not ended
      return true;
    } else if (currentDate.getTime() > endDate.getTime()){    //auction has ended
      return false;
    }
    return false;
  }

  placeBid(): void{
    if (this.itemInfo.userID !== this.userInfo.username){   //only users can bid (except owner)
      this.bid = true;
    }

  }

  confirmBid(): void {
    this.confirm = true;
  }

  bitItem(data: any): void{

    let current_date = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

    let bidData = {
      'amount': data.amount,
      'rating': data.rating,
      'time': current_date,
      'userID': this.userInfo.username,
      'item': this.itemInfo.id
    }

    console.log(bidData);

    this.itemserv.addBid(bidData).subscribe((res: any) => {console.log(res);
      if (res) {
        alert("Successful bid!");
        location.reload();
      }});
  }

  cancelBid(): void {
    this.bid = false;
  }

  unreadMessages(): boolean {
    if (this.new_messages !== 0) {
      return true;
    }
    return false;
  }

}
