import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '../../app.service';
import { Role } from '../../_models/role';


interface Status {
    value: string;
    viewValue: string;
}

@Component({
    selector: 'app-list-user',
    templateUrl: './list-user.component.html',
    styleUrls: ['./list-user.component.css']
})
export class ListUserComponent implements OnInit {
    collectionActive: any = [];
    collectionInActive: any = [];
    active = true;
    selected = 'true';
    allComplete: boolean = false;
    checked = false;
    Role: any = Role;
    success = '';

    status: Status[] = [
        { value: 'true', viewValue: 'Active' },
        { value: 'false', viewValue: 'InActive' }
    ];

    change(value: any) {
        if (value == 'false') {
            this.active = false;
        } else {
            this.active = true;
        }
    }

    constructor(private service: AppService, private router: Router) { }

    ngOnInit(): void {
        this.service.getAllData().subscribe((result: any) => {
            for (const item of result) {
                if (item.status == false)
                    this.collectionInActive.push(item);
                else
                    this.collectionActive.push(item);
            }
        });
    }
    deleteData(id: any) {
        if (confirm("Do you want to delete this account?")) {
            this.service.deleteUser(id).subscribe((result: any) => {
                for (var i = 0; i < this.collectionActive.length; i++) {
                    if (this.collectionActive[i].id === id) {
                        this.collectionInActive.splice(0, 0, this.collectionActive[i]);
                        this.collectionActive.splice(i, 1);
                    }
                }
                this.success = result.message;
            });
        }
        setTimeout(() => {
            this.success = '';
        }, 2500);
    }

    restoreData(id: any) {
        if (confirm("Do you want to restore this account?")) {
            this.service.restoreUser(id).subscribe((result: any) => {
                for (var i = 0; i < this.collectionInActive.length; i++) {
                    if (this.collectionInActive[i].id === id) {
                        this.collectionActive.splice(0, 0, this.collectionInActive[i]);
                        this.collectionInActive.splice(i, 1);
                    }
                }
                this.success = result.message;
            });
        }
        setTimeout(() => {
            this.success = '';
        }, 2500);
    }
}
