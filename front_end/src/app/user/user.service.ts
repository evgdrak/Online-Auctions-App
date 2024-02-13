import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {User} from "./user";
import {Observable, Subject} from "rxjs";

@Injectable()
export class UserService {
  private baseUrl = 'http://localhost:8080/';

  createHeaders(): HttpHeaders {
    let headers = new HttpHeaders().set("Content-Type", "application/json");
    headers = headers.append("Authorization", "Bearer " + localStorage.getItem('token'));
    headers = headers.append("Access-Control-Allow-Origin", "http://localhost:8080");
    return headers;
  }

  constructor( private http: HttpClient ) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl + 'users/', {headers: this.createHeaders()});
  }

  addUser(user: object): Observable<object> {
    return this.http.post(this.baseUrl + 'users/', user, {headers: this.createHeaders()});
  }

  getUser(uname: string): Observable<User> {
    return this.http.get<User>(this.baseUrl + 'users/' + uname, {headers: this.createHeaders()});
  }

  updateUser(uname: string, value: User): Observable<User> {
    return this.http.put<User>('http://localhost:8080/users/' + uname, value, {headers: this.createHeaders()});
  }
}
