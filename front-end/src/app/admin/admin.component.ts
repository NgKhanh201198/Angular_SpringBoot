import { Component, OnInit } from '@angular/core';
import { CurrentUser } from '../_models/current-user';
import { first } from 'rxjs/operators';
import { AuthenticationService } from '../_services/authentication.service';

@Component({
    selector: 'app-admin',
    templateUrl: './admin.component.html',
    styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
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
