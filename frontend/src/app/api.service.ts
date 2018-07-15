import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Injectable()
export class ApiService {
  localToken = '';
  //set to test / live depending if actual data exists or not
  mode = "test";

  // workaround for mockup UUISs
  currentID = 1;
  // caching variables

  currentUser = {};
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

    // TODO save auth token

    console.log("logging in with: " + user + " " + password);
    this.http.post(loginUrl, loginData, { observe : 'response'}).subscribe((resp) => {
      console.log(resp);
      if (resp.status == 200) {
        loginUrl = 'api/users/get?id=' + user;
        this.http.get(loginUrl, { observe: 'response' }).subscribe((data) => {
          console.log(data);
          this.currentUser = data.body;
          console.log(this.currentUser);
          callback(data);
        });
      }
    });

  }

  //register new user
  sendRegisterRequest = (user, password, displayname, callback) => {
    if (!user || !password) {
      console.log('sendLoginRequest needs 2 params, got ' + user + ' ' + password);
      return;
    }

    const loginUrl = 'api/users/register';
    const loginData = {
      loginName: user,
      password: password,
      displayName: displayname
    };
    // TODO save auth token
    this.http.post(loginUrl, loginData).subscribe((data) => {
      console.log(data);
      callback(data);
    });
  }

  // loads user by Id

  loadUser = (userId) => {

  }

  // adds a todo
  addTodo = (todo) => {
    //TODO actual API call & reload
    this.cachedTodos.push(todo);
  }

  // load all Tudoos for user
  loadAllTodos = () => {
    // TODO keine params?
    const getUrl = 'api/tudoos/userTudoos';

    if (this.mode === "test") {
      //inject testing objects
      let testTodo1 = {
        id: "123",
        name: "test1",
        content: "content1",
        editableBy: ["dude", "dude2"]
      }
      let testTodo2 = {
        id: "124",
        name: "test2",
        content: "content2",
        editableBy: ["dude", "dude2"]
      }
      this.cachedTodos.push(testTodo1);
      this.cachedTodos.push(testTodo2);

    } else {
      //TODO API call
    }
  }

  //returns todo by id from cached todos
  getTodoById = (id) => {
    return this.cachedTodos.find((element) => { return element.id == id });
  }
}
