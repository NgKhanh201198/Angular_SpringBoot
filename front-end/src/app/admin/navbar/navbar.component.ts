import { Component, OnInit } from '@angular/core';
import { CurrentUser } from 'src/app/_models/current-user';
import { Role } from 'src/app/_models/role';
import { AuthenticationService } from 'src/app/_services/authentication.service';
import { IsAdmin } from '../../../custom/IsAdmin';

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

  get isAdmin() {
    for (let index = 0; index < this.currentUser.roles.length; index++) {
      if (this.currentUser && this.currentUser.roles[index] === Role.ADMIN)
        return true;
    }
    return false;
  }
}
