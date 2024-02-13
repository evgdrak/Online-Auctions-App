import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

@Component({
  selector: 'app-register-details',
  templateUrl: './register-details.component.html',
  styleUrls: ['./register-details.component.css']
})
export class RegisterDetailsComponent implements OnInit {

  constructor( private router: Router ) { }

  ngOnInit(): void {
  }

  goToHomePage(): void{
    localStorage.clear();
    this.router.navigate(['home']);
  }

}
