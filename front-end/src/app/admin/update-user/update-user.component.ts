import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AppService } from 'src/app/_services/app.service';
import { Role } from 'src/app/_models/role';
import { User } from 'src/app/_models/user';
import { emailValidator } from 'src/custom/CustomValidator';
import { LogService } from '../../_services/log.service';


export interface Roles {
    name: string;
    value: string
}

@Component({
    selector: 'app-update-user',
    templateUrl: './update-user.component.html',
    styleUrls: ['./update-user.component.css']
})
export class UpdateUserComponent {
    user: User;
    roles = [];
    id: number;
    loading: boolean = false;
    submitted: boolean = false;
    success = '';
    error = '';

    rolesList: Roles[] = [
        { name: 'Quản trị viên', value: 'admin' },
        { name: 'Người điều hành', value: 'mod' },
        { name: 'Người dùng', value: 'user' }
    ];

    formUpdateData: FormGroup = this.formBuilder.group({
        username: ['', [Validators.required]],
        email: ['', [Validators.required, emailValidator()]],
        roles: [[''], [Validators.required]]
    })

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private appService: AppService,
        private logger: LogService
    ) { }

    ngOnInit(): void {
        this.id = +this.route.snapshot.paramMap.get('id');
        this.appService.getDataByID(this.id).subscribe((result: any) => {
            for (let index = 0; index < result.roles.length; index++) {
                if (result.roles[index].name === Role.ADMIN) {
                    this.roles.push('admin')
                }
                else if (result.roles[index].name === Role.MODERATOR) {
                    this.roles.push('mod')
                }
                else if (result.roles[index].name === Role.USER) {
                    this.roles.push('user')
                } else {
                    this.roles.push('')
                }
            }
            this.formUpdateData = this.formBuilder.group({
                username: [result.username, [Validators.required]],
                email: [result.email, [Validators.required, emailValidator()]],
                roles: [this.roles, [Validators.required]]
            })
        });

    }

    //Invalid error message
    get formValid() { return this.formUpdateData.controls; }
    getEmailErrorMessage(): string {
        if (this.formValid.email.errors.required) {
            return 'Email is required';
        }
        return this.formValid.email ? 'Please enter a valid email address' : '';//this.formValid.email->emailValidator()
    }
    getUserErrorMessage(): string {
        if (this.formValid.username.errors.required) {
            return 'Username is required';
        }
        return '';
    }
    getRoleErrorMessage(): string {
        if (this.formValid.roles.errors.required) {
            return 'Roles is required';
        }
        return '';
    }

    onSubmit() {
        this.submitted = true;
        return this.appService.updateUser(this.id, this.formUpdateData.value)
            .subscribe({
                next: (res) => {
                    this.error = '';
                    this.success = res.message;
                    this.logger.log(this.success);
                    this.logger.log(res);
                },
                error: (err) => {
                    this.error = err;
                    this.logger.logError(err);
                }
            }),
            setTimeout(() => {
                this.success = '';
            }, 2500);
    }
}
