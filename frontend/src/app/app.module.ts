import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NavtopComponent } from './navtop/navtop.component';
import { OverviewComponent } from './overview/overview.component';
import { TodopreviewComponent } from './todopreview/todopreview.component';
import { TodolistComponent } from './todolist/todolist.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { ProfileComponent } from './profile/profile.component';
import { GrouplistComponent } from './grouplist/grouplist.component';
import { CreditsComponent } from './credits/credits.component';
import { TodonewComponent } from './todonew/todonew.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    NavtopComponent,
    OverviewComponent,
    TodopreviewComponent,
    TodolistComponent,
    WelcomeComponent,
    ProfileComponent,
    GrouplistComponent,
    CreditsComponent,
    TodonewComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
