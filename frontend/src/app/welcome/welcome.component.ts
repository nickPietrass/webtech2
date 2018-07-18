import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  @Output() loggedIn = new EventEmitter<object>();

  loginName;
  loginPw;

  registerName;
  registerPw;

  notifyType;
  notifyContent;

  alert = false;

  constructor(private api: ApiService) { }

  ngOnInit() {
  }

  login(loginName, loginPw) {
    //TODO API Call
    //console.log({name: loginName, pass: loginPw});
    this.api.sendLoginRequest(loginName, loginPw, (status) => {

      if (status == "400") {
        this.notifyContent = "Reposnse Code 400: Something went wrong";
        this.notifyType = "alert alert-danger";
        this.alert = true;
      } else {
        this.loggedIn.emit({ name: loginName, pass: loginPw });
      }
    });
  }

  //TODO API CALL
  register(name, pw) {
    //registerName as displayName for now
    this.api.sendRegisterRequest(name, pw, name, (status) => {
      if (status == "400") {
        this.notifyContent = "Reposnse Code 400: Something went wrong";
        this.notifyType = "alert alert-danger";
        this.alert = true;
      }
    });
  }
}
