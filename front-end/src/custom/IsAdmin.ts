import { Router } from "@angular/router";
import { CurrentUser } from "src/app/_models/current-user";
import { Role } from "src/app/_models/role";
import { AuthenticationService } from "src/app/_services/authentication.service";

export class IsAdmin {
    currentUser: CurrentUser;

    constructor(private authenticationService: AuthenticationService, private router: Router) {
        this.authenticationService.currentUser.subscribe(x => { this.currentUser = x });
    }

    logout() {
        this.authenticationService.logout();
        this.router.navigate(['/login']);
    }

    //------------  phân quyền button
    get isAdmin() {
        for (let index = 0; index < this.currentUser.roles.length; index++) {
            if (this.currentUser && this.currentUser.roles[index] === Role.ADMIN)
                return true;
        }
        return false;
    }
}