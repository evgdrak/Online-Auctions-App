import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegisterGuard implements CanActivate {

  constructor( private router: Router ) { }

  /* register guard is used for the page shown after registration */
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (!(localStorage.getItem("isRegistered") === "true")){
      this.router.navigate(['register']);
      return false;
    }

    return true;
  }

}
