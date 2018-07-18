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
   
  }
  onLogin(credentials : object){
    this.isLoggedIn = true;
    this.onPageChange("todos");
  }

  onPageChange( newPage : string ){
    this.currentPage = newPage;
  }
  //TODO
  onSearch( input : string){

  }
}
