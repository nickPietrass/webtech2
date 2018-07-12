import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor() { }

  user = {
    name : "testname",
    accountName : "testaccountname",
    groups : ["g1", "g2", "g3"],
    todos: ["t1", "t2", "t3"]
  }
  ngOnInit() {
    //TODO API call to replace dummy data
  }

  onChangeName() {

  }


}
