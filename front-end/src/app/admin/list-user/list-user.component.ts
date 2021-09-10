import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {AppService} from '../../_services/app.service';
import {Role} from '../../_models/role';
import {ThemePalette} from '@angular/material/core';
import {LogService} from '../../_services/log.service';
import {map} from 'rxjs/operators';

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

    constructor(
        private service: AppService,
        private logger: LogService
    ) {
    }

    collectionActive: Array<any> = [];
    collectionInActive: Array<any> = [];
    listId: any = [];
    active = true;
    selected = 'true';
    checked = false;
    Role: any = Role;
    success = '';

    pageActive = 1;
    pageInActive = 1;
    totalLengthActive: any = 0;
    totalLengthInActive: any = 0;

    status: Status[] = [
        {value: 'true', viewValue: 'Active'},
        {value: 'false', viewValue: 'InActive'}
    ];

    // Check box
    color: ThemePalette = 'primary';
    allComplete = false;

    test: any = {
        items: [
            {
                item_id: 'XXXXXXXX',
                item: [
                    {
                        main: 'C2',
                        sub: '141',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '12200',
                        sub: '',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: null
                    },
                    {
                        main: '365200',
                        sub: '29.9',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '0',
                        sub: '2021-07-25',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '20',
                        sub: '2021-04-02T15:13:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '28.4',
                        sub: '2021-04-03T17:03:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '7.2',
                        sub: '2021-04-03T17:03:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    }
                ]
            },
            {
                item_id: 'XXXXXXXX',
                item: [
                    {
                        main: 'C1',
                        sub: '137',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '12200',
                        sub: '',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: null
                    },
                    {
                        main: '365200',
                        sub: '29.9',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '0',
                        sub: '2021-07-25',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '20',
                        sub: '2021-04-02T15:13:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '28.4',
                        sub: '2021-04-03T17:03:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    },
                    {
                        main: '7.2',
                        sub: '2021-04-03T17:03:21.12345Z',
                        mainStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        },
                        subStyle: {
                            bgColor: null,
                            color: null,
                            fontSize: null
                        }
                    }
                ]
            }
        ]
    };


    ngOnInit(): void {
        this.service.getAllData().subscribe((result: any) => {
            for (const item of result) {
                if (item.status === false) {
                    this.collectionInActive.push(item);
                    this.totalLengthInActive += 1;
                } else {
                    this.collectionActive.push(item);
                    this.totalLengthActive += 1;
                }
            }
            console.log(this.collectionActive);
        });
    }

    change(value: any): void {
        if (value === 'false') {
            this.active = false;
        } else {
            this.active = true;
        }
    }

    // Xóa tài khoản
    deleteData(id: any): void {
        if (confirm('Do you want to delete this account?')) {
            this.service.deleteUserById(id).subscribe((result: any) => {
                for (let i = 0; i < this.collectionActive.length; i++) {
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
    }

    deleteAllData(): void {
        if (this.listId.length === 0) {
            alert('You need to select at least one account!');
        } else if (confirm('Do you want to delete these accounts?')) {
            this.service.deleteMultipleUser(this.listId).subscribe((result: any) => {
                for (let i = 0; i < this.collectionActive.length; i++) {
                    // tslint:disable-next-line:prefer-for-of
                    for (let j = 0; j < this.listId.length; j++) {
                        if (this.collectionActive[i].id === this.listId[j]) {
                            this.collectionInActive.splice(0, 0, this.collectionActive[i]);
                            this.collectionActive.splice(i, 1);
                        }
                    }
                }
                this.success = result.message;
            });
        }
        setTimeout(() => {
            this.success = '';
        }, 2500);

    }

    // Khôi phục tài khoản
    restoreData(id: any): void {
        if (confirm('Do you want to restore this account?')) {
            this.service.restoreUser(id).subscribe((result: any) => {
                for (let i = 0; i < this.collectionInActive.length; i++) {
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

    // Kiểm tra id nào đã được checked
    isCheckSelectedId(): void {
        this.listId = [];
        this.collectionActive.forEach(element => {
            if (element.isSelected) {
                this.listId.push(element.id);
            }
        });
    }

    setAll(selected: boolean): void {
        this.allComplete = selected;
        if (this.collectionActive == null) {
            return;
        }
        this.collectionActive.forEach(t => t.isSelected = selected);
        this.isCheckSelectedId();
    }

    updateAllComplete(): void {
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
