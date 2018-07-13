import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ApiService {
  localToken = '';
  defaultHeaders = new HttpHeaders({
    'sessionID': this.localToken
  });
  constructor(private http: HttpClient) {
  }
  // check if an API Token is provided / a session exists
  hasToken = () => {
    if (this.localToken) {
      return true;
    }
    return false;
  }
  // login to API server
  sendLoginRequest = (user, password, callback) => {
    if (!user || !password) {
      console.log('sendLoginRequest needs 2 params, got ' + user + ' ' + password);
      return;
    }

    const loginUrl = 'api/users/login';
    const loginData = {

    };
    // TODO save auth token
    this.http.post(loginUrl, loginData).subscribe((data) => {
      console.log(data);
      callback(data);
    });
  }

  // register a new user
  registerUser = (user, password, callback) => {
    // TODO
  }

  // loads user by Id

  loadUser = (userId) => {

  }
  // load all Tudoos for user
  loadAllTodos = () => {
    // TODO keine params?
    const getUrl = 'api/tudoos/userTudoos';
  }
}
