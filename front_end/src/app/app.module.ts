import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule, Routes } from "@angular/router";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { DatePipe } from "@angular/common";

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { HomeComponent } from './home/home.component';
import { RegisterDetailsComponent } from './register-details/register-details.component';
import { RegisterGuard } from "./guards/register.guard";
import { AuthService } from "./authentication/auth.service";
import { UserListComponent } from "./user/user-list/user-list.component";
import { UserService } from "./user/user.service";
import { SellItemComponent } from "./item/sell-item/sell-item.component";
import { UpdateItemComponent } from './item/update-item/update-item.component';
import {NewMessageComponent} from "./messages/new-message/new-message.component";
import {AllMessagesComponent} from "./messages/all-messages/all-messages.component";
import {MessageContentsComponent} from "./messages/message-contents/message-contents.component";
import {LoginGuard} from "./guards/login.guard";
import { SearchComponent } from './search/search.component';
import { BidDetailsComponent } from './item/bid-details/bid-details.component';
import {BidItemComponent} from "./item/bid-item/bid-item.component";
import {UserDetailsComponent} from "./user/user-details/user-details.component";



const appRoutes: Routes = [
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'request',
    component: RegisterDetailsComponent,
    canActivate: [RegisterGuard]
  },
  {
    path: 'users',
    component: UserListComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'users/:username',
    component: UserDetailsComponent,
    canActivate: [LoginGuard]
  },
  {
    path: 'sell-item',
    /*component: SellItemComponent,*/
    children: [
      {
        path: '',
        component: SellItemComponent,
        canActivate: [LoginGuard]
      },
      {
        path: ':id',
        component: UpdateItemComponent,
        canActivate: [LoginGuard]
      }
    ]
  },
  {
    path: 'bid-item',
    /*component: BidItemComponent,*/
    children: [
    {
      path: '',
      component: BidItemComponent,
      canActivate: [LoginGuard]
    },
    {
    path: ':id',
    component: BidDetailsComponent
    }
    ]
  },
  {
    path: 'search',
    component: SearchComponent
  },
  {
    path: 'messages',
    children: [
      {
        path: 'user/:username',
        component:AllMessagesComponent,
        canActivate: [LoginGuard]
      },
      {
        path: '',
        component: NewMessageComponent,
        canActivate: [LoginGuard]
      },
      {
        path: ':id',
        component: MessageContentsComponent,
        canActivate: [LoginGuard]
      }
    ]
  }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    HomeComponent,
    RegisterDetailsComponent,
    UserListComponent,
    UserDetailsComponent,
    SellItemComponent,
    BidItemComponent,
    UpdateItemComponent,
    NewMessageComponent,
    AllMessagesComponent,
    MessageContentsComponent,
    SearchComponent,
    BidDetailsComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(appRoutes),
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [AuthService, UserService, DatePipe],
  bootstrap: [AppComponent]
})
export class AppModule { }
