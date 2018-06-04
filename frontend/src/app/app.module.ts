import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NavtopComponent } from './navtop/navtop.component';
import { OverviewComponent } from './overview/overview.component';
import { TodopreviewComponent } from './todopreview/todopreview.component';
import { TodolistComponent } from './todolist/todolist.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    NavtopComponent,
    OverviewComponent,
    TodopreviewComponent,
    TodolistComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
