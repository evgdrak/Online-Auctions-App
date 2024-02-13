import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {Messages, MessagesList} from "./messages";

@Injectable({
  providedIn: 'root'
})
export class MessagesService {

  constructor( private http: HttpClient ) { }

  private baseUrl = 'http://localhost:8080/messages/';

  createHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set("Content-Type", "application/json");
    headers = headers.append("Authorization", "Bearer " + localStorage.getItem('token'));
    headers = headers.append("Access-Control-Allow-Origin", "http://localhost:8080");
    return headers;
  }

  addMessage(messages: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, messages, {headers: this.createHeaders()});
  }

  updateMessage(messages: any, id: number): Observable<any> {
    return this.http.put<any>(this.baseUrl + id, messages, {headers: this.createHeaders()});
  }

  getMessages(params: HttpParams): Observable<MessagesList>{
    return this.http.get<MessagesList>(this.baseUrl + 'user/' + localStorage.getItem('username'), {headers: this.createHeaders(), params: params});
  }

  getUnreadMessages(): Observable<number>{
    return this.http.get<number>(this.baseUrl + 'user/' + localStorage.getItem('username') + '/unread', {headers: this.createHeaders()});
  }

  getMessage(id: number): Observable<Messages>{
    return this.http.get<Messages>(this.baseUrl + id, {headers: this.createHeaders()});
  }

  deleteMessage(id: number): Observable<any>{
    return this.http.delete<any>(this.baseUrl + id, {headers: this.createHeaders()});
  }

}
