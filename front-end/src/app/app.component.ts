import {Component} from '@angular/core';
import {AuthenticationService} from './_services/authentication.service';
import {Router} from '@angular/router';
import {CurrentUser} from './_models/current-user';
import {Role} from './_models/role';
import {OnInit} from '@angular/core';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
    currentUser: CurrentUser;
    show = true;

    constructor(private authenticationService: AuthenticationService, private router: Router) {
        this.authenticationService.currentUser.subscribe(x => {
            this.currentUser = x;
        });
        setTimeout(() => {
            // this.show = false;
        }, 1500);
    }

    ngOnInit(): void {
        this.currentUser = this.authenticationService.currentUserValue;
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }

    //------ phân quyền button ----------
    get isAdmin() {
        for (let index = 0; index < this.currentUser.roles.length; index++) {
            if (this.currentUser && this.currentUser.roles[index] === Role.ADMIN) {
                return true;
            }
        }
        return false;
    }

}
