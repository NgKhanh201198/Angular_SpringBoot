import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { AppService } from 'src/app/app.service';
import { Location } from '@angular/common';
import { emailValidator } from 'src/custom/CustomValidator';
import { ViewChild } from '@angular/core';
import { Router } from '@angular/router';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

    @ViewChild('myForm') myForm: NgForm;
    formData: FormGroup;
    loading = false;
    submitted = false;
    success = '';
    error = '';
    hide = true;

    // rolesList: Roles[] = [
    //     { name: 'Quản trị viên', value: 'admin' },
    //     { name: 'Người điều hành', value: 'mod' },
    //     { name: 'Người dùng', value: 'user' }
    // ];


    constructor(
        private formBuilder: FormBuilder,
        private service: AppService,
        private router: Router
    ) { }

    ngOnInit() {
        this.formData = this.formBuilder.group({
            username: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9_]{3,18}')]],
            email: ['', [Validators.required, emailValidator()]],
            password: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9_@]{6,18}')]],
            // roles: ['', [Validators.required]]
        });
    }

    //Invalid error message
    get formValid() { return this.formData.controls; }
    getUserErrorMessage(): string {
        if (this.formValid.username.errors.required) {
            return 'Username is required';
        }
        return this.formValid.username.errors.pattern ? 'Please enter a valid username' : '';
    }
    getEmailErrorMessage(): string {
        if (this.formValid.email.errors.required) {
            return 'Email is required';
        }
        return this.formValid.email ? 'Please enter a valid email address' : '';//this.formValid.email->emailValidator()
    }
    getPasswordErrorMessage(): string {
        if (this.formValid.password.errors.required) {
            return 'Password is required';
        }
        return this.formValid.password.errors.pattern ? 'Please enter a valid password' : '';
    }
    // getRoleErrorMessage(): string {
    //     if (this.formValid.roles.errors.required) {
    //         return 'Roles is required';
    //     }
    //     return '';
    // }

    //Submit
    onSubmit() {
        this.submitted = true;
        this.loading = true;


        return this.service.addUser(this.formData.value)
            .subscribe({
                next: (result) => {
                    this.loading = false,
                        this.submitted = false,
                        this.error = '';
                    this.myForm.resetForm();
                    this.success = result.message
                    console.warn(this.success);

                },
                error: error => {
                    this.error = error;
                    this.loading = false;
                }
            }),
            setTimeout(() => {
                this.success = '';
            }, 3000),
            setTimeout(() => {
                this.router.navigate(['/login']);
            }, 3000);
    }

}
