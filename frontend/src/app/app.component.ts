import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
}) 
export class AppComponent implements OnInit{
  title = 'app';
  //TODO actually check this
  isLoggedIn = false;
  currentPage = "login";

  ngOnInit() {
    console.log(this.currentPage)
  }
  onLogin(credentials : object){
    this.isLoggedIn = true;
    this.onPageChange("home");
  }

  onPageChange( newPage : string ){
    console.log("APP switching to " + newPage)
    this.currentPage = newPage;
  }
  //TODO
  onSearch( input : string){

  }
}
