import { Component, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-navtop',
  templateUrl: './navtop.component.html',
  styleUrls: ['./navtop.component.css']
})
export class NavtopComponent implements OnInit {
  @Output() pageChange
  loginName = "";
  loginPw = "";

  constructor() { }
  loggedIn = false;

  ngOnInit() {
  }

  onLoginClick(){
    //TODO replace with actual check
    this.loggedIn = !this.loggedIn;

  }

}
