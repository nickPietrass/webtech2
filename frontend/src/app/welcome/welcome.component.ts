import { Component, OnInit , EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  @Output() loggedIn = new EventEmitter<object>();

  constructor() { }

  ngOnInit() {
  }
  
  login(loginName, loginPw){
    //TODO API Call
    console.log({name: loginName, pass: loginPw});
    this.loggedIn.emit({name: loginName, pass: loginPw});
  }

  //TODO API CALL
  register(name, pw){
    
  }
}
