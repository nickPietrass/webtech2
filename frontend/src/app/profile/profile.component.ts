import { Component, OnInit } from '@angular/core';
import { ApiService } from '../api.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  constructor(private api: ApiService) { }

  notifyType;
  notifyContent;

  notify = false;

  deleteConfirmShow = false;

  
  ngOnInit() {
   

  }

  onNewNameClick = (name) => {
    this.api.changeDisplayname(name, (resp) => {

      if (resp.status == "400") {
        this.notifyContent = "Something went wrong";
        this.notifyType = "alert alert-danger";
        this.notify = true;
      }
      if (resp.status == "200") {
        this.notifyContent = "Changed DisplayName successfully";
        this.notifyType = "alert alert-success";
        this.notify = true;
      }

    });
  }

  onNewPasswordClick = (pw) => {
    this.api.changePassword(pw, (resp) => {
      if (resp.status == "400") {
        this.notifyContent = "Something went wrong";
        this.notifyType = "alert alert-danger";
        this.notify = true;
      }
      if (resp.status == "200") {
        this.notifyContent = "Changed Password successfully";
        this.notifyType = "alert alert-success";
        this.notify = true;
      }
    })
  }

}
