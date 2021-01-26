import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from '../_services/authentication.service';
import { first } from 'rxjs/operators';
import { LogService } from '../_services/log.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    error = '';

    constructor(
        private formBuilder: FormBuilder,
        private authenticationService: AuthenticationService,
        private route: ActivatedRoute,
        private router: Router,
        private logger: LogService
    ) {
        if (this.authenticationService.currentUserValue) {
            this.router.navigate(['/']);
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group({
            username: ['', Validators.required],
            password: ['', Validators.required]
        });
    }

    get loginValid() { return this.loginForm.controls; }

    onSubmit() {

        this.submitted = true;

        // stop here if form is invalid
        if (this.loginForm.invalid) {
            return;
        }
        this.loading = true;


        this.authenticationService.login(this.loginForm.value.username, this.loginForm.value.password)
            .pipe(first())
            .subscribe(
                {
                    next: () => {
                        this.logger.log("Login success");
                        // get return url from route parameters or default to '/'
                        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
                        this.router.navigate([returnUrl]);
                    },
                    error: (error: any) => {
                        this.logger.logError(error);
                        this.error = error;
                        this.loading = false;
                    }
                }
            )

    }
}
