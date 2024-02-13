import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from "@angular/common/http";
import {map, Observable, throwError} from "rxjs";
import {catchError} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor( private http: HttpClient ) { }

  registerUser(user: any): Observable<any> {                 //register user --> alert user accordingly in case of error
    const url = 'http://localhost:8080/register';
    return this.http.post<any>(url, user, { observe: 'response' })
      .pipe(map(user => { return user; }),
        catchError(err => {
          if (err.status === 400) {
            alert(JSON.stringify(err.error));
          } else if (err.status === 500){
              alert("Username is already taken, please choose a new one.");
            }
          const error = err.error.message || err.statusText;
          return throwError(error);
        }))
  }

  loginUser(user: any): Observable<any> {         //login user --> alert user accordingly in case of error
    const url = 'http://localhost:8080/login';
    return this.http.post<any>(url, user)
      .pipe(catchError(err => {
        if (err.status === 401 || err.status === 403){
          alert("Wrong username or password, please try again.");
        } else if (err.status === 400){
          alert("Your registration request is still pending for approval by the admin.");
        }
        const error = err.error.message || err.statusText;
        return throwError(error);
      }));
  }

  logoutUser(): void{                       //logout user --> update local storage
    localStorage.removeItem('token');
    localStorage.setItem('isLoggedIn', 'false');
    localStorage.removeItem('username');
  }

}
