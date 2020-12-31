import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../_models/current-user';
import { AuthenticationService } from '../_services/authentication.service';

@Component({
    selector: 'app-profile',
    templateUrl: './profile.component.html',
    styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

    loading: Boolean = false;
    currentUser: CurrentUser;
    userFromApi: CurrentUser[] = [];

    constructor(private authenticationService: AuthenticationService) {

    }

    ngOnInit() {
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
            this.currentUser = this.authenticationService.currentUserValue;
        }, 800);
    }

}
