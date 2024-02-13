import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor( private router: Router ) { }

  /* login guard prevents guests from having access to pages only for users (e.g. user profile, sell item) */
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!(localStorage.getItem("isLoggedIn") === "true")){      //not a user
      this.router.navigate(['home']);
      return false;
    }

    return true;

  }

}
