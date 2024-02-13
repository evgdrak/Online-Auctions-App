import { Component, OnInit } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import { DatePipe } from "@angular/common";
import {AuthService} from "../../authentication/auth.service";
import {UserService} from "../../user/user.service";
import {Item} from "../item";
import {ItemService} from "../item.service";
import {MessagesService} from "../../messages/messages.service";
import * as L from "leaflet";
import {LayerGroup} from "leaflet";
import {DomSanitizer} from "@angular/platform-browser";


@Component({
  selector: 'app-update-item',
  templateUrl: './update-item.component.html',
  styleUrls: ['./update-item.component.css']
})
export class UpdateItemComponent implements OnInit {

  itemInfo: any = {};
  userInfo: any = {};
  updateForm: any = FormGroup;
  p_names: any = [];
  searchText: string = '';
  now: string | null = this.datepipe.transform(new Date(), 'yyyy-MM-dd hh:mm');

  new_messages: number = 0;

  map!: L.Map | LayerGroup<any>;

  xlng : number = 0;
  xlat : number = 0;
  marker_count : number = 0;
  marker!: L.Marker<any>;

  constructor( private itemserv: ItemService, private messagesService: MessagesService, private authserv: AuthService, private userserv: UserService,
               private route: ActivatedRoute, private router: Router, private fb: FormBuilder, private datepipe: DatePipe, private sanitizer: DomSanitizer) {
  }

  ngOnInit(): void {
    this.messagesService.getUnreadMessages()
      .subscribe((response) => {this.new_messages = response});
    const id = this.route.snapshot.paramMap.get('id');

    this.updateForm = this.fb.group({
      name: ['',Validators.required],
      categories: this.fb.array([this.fb.group({"name": ''})]),
      location: ['',Validators.required],
      latitude: [''],
      longitude: [''],
      country: ['',Validators.required],
      buy_price: ['',Validators.required],
      first_bid: ['',Validators.required],
      ends: ['',Validators.required],
      rating: ['',Validators.required],
      description: ['',Validators.required],
      old_photos: this.fb.array([this.fb.group({"id": '', "img": '', "url": ''})]),
      new_photos: this.fb.array([this.fb.group({"img": '', "url": ''})])
    });

    this.getUserInfo();

    this.getItem(Number(id));

  }

  private initMap(): void {
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
    this.marker = L.marker([ this.xlat, this.xlng ], {icon: myIcon, draggable: true}).addTo(this.map);
    this.marker.on('dragend', () => {
      this.xlng = this.marker.getLatLng().lat;
      this.xlat = this.marker.getLatLng().lng;
    });
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

  get categories(): FormArray{
    return this.updateForm.controls["categories"] as FormArray;
  }

  loadCategories(): void{
    this.removeCategory(0);
    for (let idx of this.itemInfo.categories){
      this.categories.push(this.fb.group({"name": idx.name}));
    }
  }

  addCategory(){
    this.categories.push(this.fb.group({"name": ''}));
  }

  removeCategory(i: number){
    this.categories.removeAt(i);
  }

  get old_photos(): FormArray{
    return this.updateForm.controls["old_photos"] as FormArray;
  }

  get new_photos(): FormArray{
    return this.updateForm.controls["new_photos"] as FormArray;
  }

  loadPhotos(): void{
    this.removeOldPhoto(0);
    let i=0;
    for (let p of this.itemInfo.photos){
      this.p_names.push(p.name);
      const id = p.id;
      let res = this.itemserv.createPhotos(this.itemInfo);
      console.log(res[0])
      this.old_photos.push(this.fb.group({"id": id, "img": res[0].img, "url": res[0].url}));
      console.log(this.old_photos.value)
      i++;
    }
  }

  addPhoto(){
    this.new_photos.push(this.fb.group({"img": '', "url": ''}));
  }

  removeOldPhoto(i: number){
    delete this.p_names[i];
    this.old_photos.removeAt(i);
  }

  removeNewPhoto(i: number){
    this.new_photos.removeAt(i);
  }

  getItem(id: number): void{
    this.itemserv.getItem(Number(id)).subscribe(it => {
      console.log(it);
      this.itemInfo = it;

      this.updateForm = this.fb.group({
        name: [it.name, Validators.required],
        categories: this.fb.array([this.fb.group({"name": ''})]),
        location: [it.location, Validators.required],
        latitude: [it.latitude],
        longitude: [it.longitude],
        country: [it.country, Validators.required],
        buy_price: [it.buy_price, Validators.required],
        first_bid: [it.first_bid, Validators.required],
        ends: [it.ends, Validators.required],
        rating: [it.rating, Validators.required],
        description: [it.description, Validators.required],
        old_photos: this.fb.array([this.fb.group({"id": '', "img": '', "url": ''})]),
        new_photos: this.fb.array([this.fb.group({"img": '', "url": ''})])
      });
      this.loadCategories();
      this.loadPhotos();
      this.xlat = it.latitude;
      this.xlng = it.longitude;
      this.initMap();
    });
  }



  updateItem(data: any): void{
    let allPhotos: any = [];

    for (let i=0; i<this.old_photos.length; i++){
      allPhotos.push(this.old_photos.at(i).value);
    }
    console.log(allPhotos)

    for (let i=0; i<this.new_photos.length; i++){
      allPhotos.push(this.new_photos.at(i).value);
    }

    console.log(allPhotos)

    let itemData = {
      'id': this.itemInfo.id,
      'name': data.name,
      'categories': data.categories,
      'location': data.location,
      'latitude': this.xlat,
      'longitude': this.xlng,
      'country': data.country,
      'buy_price': data.buy_price,
      'first_bid': data.first_bid,
      'ends': data.ends,
      'rating': data.rating,
      'description': data.description,
      'photos': allPhotos
    }


    console.log(itemData);

    const formData = new FormData();

    formData.append('item', new Blob([JSON.stringify(itemData)], {type: 'application/json'}));
    console.log(formData)
    for (let i=0; i<allPhotos.length; i++){
      if (allPhotos[i].img != ''){
        formData.append('photo', allPhotos[i].img, allPhotos[i].img.name);
      }

    }

    this.itemserv.updateItem(this.itemInfo.id, formData).subscribe((response: any) => {
      console.log(response);
      this.router.navigateByUrl('/', {skipLocationChange: true})
        .then(() => this.router.navigate(['/sell-item']));
    });
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

      this.new_photos.at(i).setValue({'img': img, 'url': url});
      console.log(this.new_photos.value)


    }
  }

}
