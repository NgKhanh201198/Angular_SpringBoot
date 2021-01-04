import { Component, OnInit } from '@angular/core';
import { first, delay } from 'rxjs/operators';
import { CurrentUser } from '../_models/current-user';
import { AuthenticationService } from '../_services/authentication.service';
import { UserService } from '../_services/user.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
    loading: Boolean = false;
    currentUser: CurrentUser;
    userFromApi: CurrentUser;

    constructor(
        private authenticationService: AuthenticationService
    ) {
        this.currentUser = this.authenticationService.currentUserValue;
    };

    ngOnInit() {
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
            this.userFromApi = this.currentUser;
        }, 800);

    };
}
