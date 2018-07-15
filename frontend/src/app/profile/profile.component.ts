import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(private api: ApiService) { }
  user;

  ngOnInit() {
    //TODO API call to replace dummy data
    
  }

  onChangeName() {

  }


}
