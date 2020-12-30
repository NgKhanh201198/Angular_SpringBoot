import { Component, OnInit } from '@angular/core';
import { CurrentUser } from 'src/app/_models/current-user';
import { AuthenticationService } from 'src/app/_services/authentication.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  currentUser: CurrentUser;
  userFromApi: CurrentUser[] = [];
  constructor(private authenticationService: AuthenticationService) { }

  ngOnInit(): void {
    this.currentUser = this.authenticationService.currentUserValue;    
  }

}
