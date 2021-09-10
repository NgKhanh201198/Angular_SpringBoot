import {Component, OnInit} from '@angular/core';
import {CurrentUser} from '../_models/current-user';
import {AuthenticationService} from '../_services/authentication.service';
import {CookieService} from 'ngx-cookie-service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {
    loading = false;
    currentUser: CurrentUser;
    userFromApi: CurrentUser;

    constructor(
        private authenticationService: AuthenticationService,
        private cookieService: CookieService
    ) {
        this.currentUser = this.authenticationService.currentUserValue;
    }

    ngOnInit(): void {
        this.loading = true;
        setTimeout(() => {
            this.loading = false;
            this.userFromApi = this.currentUser;
        }, 800);

        // test cookies
        const expiredDate = new Date();
        expiredDate.setDate(expiredDate.getDate() + 1);
        this.cookieService.set('new', 'việt nam', expiredDate);

        console.log(('new'));
        console.log(this.cookieService.get('new'));
        console.log(this.cookieService.get('1P_JAR'));
    }
}
