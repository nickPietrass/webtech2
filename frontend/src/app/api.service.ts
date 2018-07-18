import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ApiService {
  localToken = '';
  //set to test / live depending if actual data exists or not
  mode = "!test";

  // workaround for mockup UUISs
  currentID = 1;
  // caching variables

  currentUser;
  cachedTodos = [];

  defaultHeaders = new HttpHeaders({
    'sessionID': this.localToken
  });
  constructor(private http: HttpClient) {

  }

  //mockup for UUID generation
  getNewId = () => {
    this.currentID++;
    return this.currentID;
  }
  getCachedTodos = () => {
    return this.cachedTodos;
  }
  // check if an API Token is provided / a session exists
  hasToken = () => {
    if (this.localToken) {
      return true;
    }
    return false;
  }

  // logout

  logout = () => {
    this.currentUser = null;
  }
  // login to API server
  sendLoginRequest = (user, password, callback) => {
    if (!user || !password) {
      console.log('sendLoginRequest needs 2 params, got ' + user + ' ' + password);
      return;
    }

    let loginUrl = 'api/users/login';
    let loginData = {
      loginName: user,
      password: password
    };

    console.log("logging in with: " + user + " " + password);
    this.http.post(loginUrl, loginData, { observe: 'response' }).subscribe((resp) => {
      console.log(resp);
      if (resp.status == 200) {
        loginUrl = 'api/users/get?id=' + user;
        this.http.get(loginUrl, { observe: 'response' }).subscribe((data) => {
          console.log(data);
          this.currentUser = data.body;
          console.log(this.currentUser);
        });
      }
      callback(resp.status);
    });

  }

  //register new user
  sendRegisterRequest = (user, password, displayname, callback) => {
    if (!user || !password) {
      console.log('sendRegisterRequest needs 2 params, got ' + user + ' ' + password);
      return;
    }

    const loginUrl = 'api/users/register';
    const loginData = {
      loginName: user,
      password: password,
      displayName: displayname
    };
    // TODO save auth token
    this.http.post(loginUrl, loginData, { observe: "response" }).subscribe((data) => {
      console.log(data);
      callback(data);
    });
  }

  // loads user by Id

  reloadCurrentUser = (callback) => {
    let loginUrl = 'api/users/get?id=' + this.currentUser.loginName;
    this.http.get(loginUrl, { observe: 'response' }).subscribe((data) => {
      this.currentUser = data.body;
      callback(data);
    });

  }

  changeDisplayname = (name, callback) => {
    this.http.put("api/users/editDisplayName", name, { observe: "response" }).subscribe((data) => {
      this.reloadCurrentUser(callback);
    });
  }
  changePassword = (pw, callback) => {
    this.http.put("api/users/editPassword", pw, { observe: "response" }).subscribe((data) => {
      this.reloadCurrentUser(callback);
    });
  }
  // adds a todo
  addTodo = (todo) => {
    //TODO actual API call & reload
    this.cachedTodos.push(todo);
  }

  // load all Tudoos for user
  loadAllTodos = (callback) => {

    const getUrl = 'api/tudoos/userTudoos';
    this.http.get(getUrl, { observe: 'response' }).subscribe((data) => {
      console.log(data)
    });
  }

  //returns todo by id from cached todos
  getTodoById = (id) => {
    return this.cachedTodos.find((element) => { return element.id == id });
  }
}
