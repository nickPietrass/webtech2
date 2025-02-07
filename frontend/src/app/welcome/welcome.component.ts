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

  register(name, pw) {

    if (!name.includes("@") || !name.includes(".")) {
     
        this.notifyContent = "Name has to be a valid email address";
        this.notifyType = "alert alert-danger";
        this.alert = true;
        return;
      }
    //registerName as displayName for now
    this.api.sendRegisterRequest(name, pw, name, (status) => {
      console.log(status)
      if (status == "400") {
        this.notifyContent = "Reponse Code 400: Something went wrong";
        this.notifyType = "alert alert-danger";
        this.alert = true;
      }
      if (status == "200") {
        this.notifyContent = "Account created!";
        this.notifyType = "alert alert-success";
        this.alert = true;
      }
    });
  }
}
