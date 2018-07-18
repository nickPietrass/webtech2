import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { checkAndUpdatePureExpressionDynamic } from '../../node_modules/@angular/core/src/view/pure_expression';
import { identifierModuleUrl, TransitiveCompileNgModuleMetadata } from '../../node_modules/@angular/compiler';

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
    this.http.post(loginUrl, loginData, { observe: 'response' }).subscribe((resp) => {
  
      if (resp.status == 200) {
        loginUrl = 'api/users/get?id=' + user;
        this.http.get(loginUrl, { observe: 'response' }).subscribe((data) => {

          this.currentUser = data.body;
  
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
    this.http.post(loginUrl, loginData, { observe: "response" }).subscribe((data) => {
      callback(data.status);
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
  addTodo = (todo, callback) => {

    this.http.post("api/tudoos/create", todo, { observe: 'response' }).subscribe((data) => {

      if (callback)
        this.loadAllTodos(callback);
      else
        this.loadAllTodos(() => { });
    });

  }

  deleteTodo = (id, callback) => {

    this.http.delete("api/tudoos/remove?id=" + id, { observe: 'response' }).subscribe((data) => {
      this.loadAllTodos(() => {});
      if (callback)
        callback()
    });

  }

  editTodo = (id, title, content, callback) =>{
    let todo = {
      tudooUUID : id,
      title : title,
      content : content
    }
    this.http.put("api/tudoos/editText", todo, { observe: 'response' }).subscribe((data) => {
      this.loadAllTodos();
      if(callback)
        callback();
    });
  }
  // load all Tudoos for user
  loadAllTodos = (callback  = () => {}) => {
    const getUrl = 'api/tudoos/userTudoos';
    this.http.get(getUrl, { observe: 'response' }).subscribe((data) => {
      this.cachedTodos = data.body as object[];
    });
  }

  //returns todo by id from cached todos
  getTodoById = (id) => {
    return this.cachedTodos.find((element) => { return element.tudooUUID == id });
  }

  logout = (callback) => {
    this.http.post("api/users/logout", "", { observe: "response" }).subscribe((data) => {
      this.currentUser = null;
      if (callback)
        callback(data);
    });
  }

  deleteAccount = () => {
    this.http.delete("api/users/remove", { observe: "response" }).subscribe((data) => {
      this.currentUser = null;
    });
  }
}
