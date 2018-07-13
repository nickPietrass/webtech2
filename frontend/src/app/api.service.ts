import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ApiService {
  localToken = "";
  defaultHeaders = new HttpHeaders({
    'apiToken': this.localToken
  })
  constructor(private http: HttpClient) {
  }
  //check if an API Token is provided / a session exists
  hasToken = () => {
    if (this.localToken)
      return true;
    return false;
  }
  //login to API server
  sendLoginRequest = (user, password) => {
    if(!user || !password){
      console.log("sendLoginRequest needs 2 params, got " + user + " " + password);
      return;
    }
    let loginUrl = "api/users/login";
    let loginData = {

    }
    this.http.post(loginUrl, loginData).subscribe((data) => {

    })
  }
}
