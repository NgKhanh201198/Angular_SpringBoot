import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '../../_services/app.service';
import { Role } from '../../_models/role';
import { ThemePalette } from '@angular/material/core';
import { LogService } from '../../_services/log.service';
import { map } from 'rxjs/operators';

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
    collectionActive: Array<any> = [];
    collectionInActive: Array<any> = [];
    listId: any = [];
    active = true;
    selected = 'true';
    checked = false;
    Role: any = Role;
    success = '';

    pageActive: number = 1;
    pageInActive: number = 1;
    totalLengthActive: any = 0;
    totalLengthInActive: any = 0;

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
    };

    constructor(
        private service: AppService,
        private logger: LogService
    ) { };

    ngOnInit() {
        this.service.getAllData().subscribe((result: any) => {
            for (const item of result) {
                if (item.status == false) {
                    this.collectionInActive.push(item);
                    this.totalLengthInActive += 1;
                }
                else {
                    this.collectionActive.push(item);
                    this.totalLengthActive += 1;
                }
            }
        });
    };

    //Xóa tài khoản
    deleteData(id: any) {
        if (confirm("Do you want to delete this account?")) {
            this.service.deleteUserById(id).subscribe((result: any) => {
                for (var i = 0; i < this.collectionActive.length; i++) {
                    if (this.collectionActive[i].id === id) {
                        this.collectionInActive.splice(0, 0, this.collectionActive[i]);
                        this.collectionActive.splice(i, 1);
                    }
                }
                this.success = result.message;
                this.logger.log(this.success);
            });
        }
        setTimeout(() => {
            this.success = '';
        }, 2500);
    };

    deleteAllData() {
        if (this.listId.length == 0) {
            alert("You need to select at least one account!");
        }
        else if (confirm("Do you want to delete these accounts?")) {
            this.service.deleteMultipleUser(this.listId).subscribe((result: any) => {
                for (var i = 0; i < this.collectionActive.length; i++) {
                    for (let j = 0; j < this.listId.length; j++) {
                        if (this.collectionActive[i].id === this.listId[j]) {
                            this.collectionInActive.splice(0, 0, this.collectionActive[i]);
                            this.collectionActive.splice(i, 1);
                        }
                    }
                }
                this.success = result.message;
            });
        };
        setTimeout(() => {
            this.success = '';
        }, 2500);

    }

    // Khôi phục tài khoản
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
        };
        setTimeout(() => {
            this.success = '';
        }, 2500);
    };

    //Check box
    color: ThemePalette = 'primary';
    allComplete: boolean = false;

    //Kiểm tra id nào đã được checked
    isCheckSelectedId() {
        this.listId = [];
        this.collectionActive.forEach(element => {
            if (element.isSelected) {
                this.listId.push(element.id);
            }
        });
    }

    setAll(selected: boolean) {
        this.allComplete = selected;
        if (this.collectionActive == null) {
            return;
        }
        this.collectionActive.forEach(t => t.isSelected = selected);
        this.isCheckSelectedId();
    }
    updateAllComplete() {
        this.allComplete = this.collectionActive != null && this.collectionActive.every(t => t.isSelected);
        this.isCheckSelectedId();
    }
    someComplete(): boolean {
        if (this.collectionActive == null) {
            return false;
        }
        return this.collectionActive.filter(t => t.isSelected).length > 0 && !this.allComplete;
    }
}
