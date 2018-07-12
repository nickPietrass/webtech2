import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  @Output() pageChange = new EventEmitter<string>();
  pages: Array<Object> = [];

  constructor() { }
  

  ngOnInit() {

    // test data
    

    this.pages.push({ name : 'Login', newPage : 'login'});
    this.pages.push({ name : 'Profile', newPage : 'profile'});
    this.pages.push({ name : 'TuDoos', newPage : 'todos'});
    this.pages.push({ name : 'Groups', newPage : 'groups'});
    this.pages.push({ name : 'Credits', newPage : 'credits'});

    console.log("navbar init")

  }

  onPageClick( newPage : string){
    console.log("switching to " + newPage);
    this.pageChange.emit(newPage);
  }
}

/* subcategory
 <h6 class="sidebar-heading d-flex justify-content-between align-items-center px-3 mt-4 mb-1 text-muted">
          <span>Saved reports</span>
          <a class="d-flex align-items-center text-muted" href="#">
            <span data-feather="plus-circle"></span>
          </a>
        </h6>

  subcategory list
  <ul class="nav flex-column mb-2">
          <li class="nav-item">
            <a class="nav-link" href="#">
              <span data-feather="file-text"></span>
              Current month
            </a>
          </li>
  </ul>
*/
