import { Component, OnInit, Output } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-navtop',
  templateUrl: './navtop.component.html',
  styleUrls: ['./navtop.component.css']
})
export class NavtopComponent implements OnInit {
  @Output() pageChange
  loginName = "";
  loginPw = "";

  constructor(private api: ApiService) { }

  ngOnInit() {
  }

  login(name, pw) {
    this.api.sendLoginRequest(name, pw, () => { });

  }

}
