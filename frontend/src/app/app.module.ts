import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';


import { AppComponent } from './app.component';
import { NavbarComponent } from './navbar/navbar.component';
import { NavtopComponent } from './navtop/navtop.component';
import { OverviewComponent } from './overview/overview.component';
import { TodoviewComponent } from './todoview/todoview.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    NavtopComponent,
    OverviewComponent,
    TodoviewComponent
  ],
  imports: [
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
