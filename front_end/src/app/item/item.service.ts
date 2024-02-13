import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, map} from "rxjs";
import {Item, ItemList} from "./item";
import {Bid} from "./bid";
import {DomSanitizer} from "@angular/platform-browser";

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  constructor( private http: HttpClient, private sanitizer: DomSanitizer ) { }

  private baseUrl = 'http://localhost:8080/items';
  private baseUrl2 = 'http://localhost:8080/bids';

  createHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set("Content-Type", "application/json");
    headers = headers.append("Authorization", "Bearer " + localStorage.getItem('token'));
    headers = headers.append("Access-Control-Allow-Origin", "http://localhost:8080");
    return headers;
  }

  createHeaders2(): HttpHeaders {
    let headers = new HttpHeaders();
    headers = headers.append("Authorization", "Bearer " + localStorage.getItem('token'));
    headers = headers.append("Access-Control-Allow-Origin", "http://localhost:8080");
    return headers;
  }

  addItem(item: FormData): Observable<any> {
    return this.http.post<any>(this.baseUrl, item, {headers: this.createHeaders2()});
  }

  getItems(queryParams: any): Observable<ItemList>{
    return this.http.get<any>(this.baseUrl, {params: queryParams})
      .pipe(map(res => {return res;} ));
  }

  getItemsWithBidsFromUser(username: string): Observable<ItemList>{
    return this.http.get<any>(this.baseUrl + '/bids/' + username, {headers: this.createHeaders()})
      .pipe(map(res => {return res;} ));
  }

  getItem(id: number): Observable<Item>{
    return this.http.get<Item>(this.baseUrl + '/' + id);
  }

  updateItem(id: number, data: any): Observable<any>{
    return this.http.put<any>(this.baseUrl + '/' + id, data, {headers: this.createHeaders2()});
  }

  deleteItem(id: number): Observable<any>{
    return this.http.delete<any>(this.baseUrl + '/' + id, {headers: this.createHeaders()});
  }

  addBid(bid: any): Observable<any>{
    return this.http.post<any>(this.baseUrl2, bid, {headers: this.createHeaders()});
  }



  createPhotos(item: Item): any{
    const photos = item.photos;
    let res: any = [];

    for (let i=0; i<photos.length; i++){
      const photoFileData = photos[i];
      const photoBlob = this.dataToBlob(photoFileData.image, photoFileData.type);

      const photoFile = new File([photoBlob], photoFileData.name, {type: photoFileData.type});

      const finalFile = {img: photoFile, url: this.sanitizer.bypassSecurityTrustUrl(window.URL.createObjectURL(photoFile))};
      res.push(finalFile);
    }

    return res;
  }

  dataToBlob(image: any, type: string){
    const byteString = window.atob(image);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);

    for (let i=0; i<byteString.length; i++){
      int8Array[i] = byteString.charCodeAt(i);
    }

    const blob = new Blob([int8Array], {type: type});
    return blob;
  }

}
