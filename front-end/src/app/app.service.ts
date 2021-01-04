import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable, of } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
})
export class AppService {
    urlUser = `${environment.url}` + 'api/user';
    urlRestoreUser = `${environment.url}` + 'api/user/restore';
    urlBehavior = `${environment.url}` + 'api/behavior';
    urlAddUser = `${environment.url}` + 'api/auth/register';

    constructor(private http: HttpClient) { }

    // User
    getAllData(): Observable<any> {
        return this.http.get(this.urlUser).pipe(
            tap(response => { }),
            catchError(error => of(console.log(error)))
        );
    }
    getDataByID(id: number): Observable<any> {
        return this.http.get(`${this.urlUser}/${id}`).pipe(
            tap(response => { }),
            catchError(error => of(console.log(error)))
        );
    }

    addUser(data: any): Observable<any> {
        return this.http.post(this.urlAddUser, data, httpOptions)
            .pipe(
                tap(response => { }),
                catchError((error) => {
                    return throwError(error);
                })
            );
    }

    updateUser(id: any, data: any): Observable<any> {
        return this.http.put(`${this.urlUser}/${id}`, data, httpOptions)
            .pipe(
                tap(response => { }),
                catchError(error => of(console.log(error)))
            );
    }

    deleteUserById(id: any): Observable<any> {
        return this.http.delete(`${this.urlUser}/${id}`)
            .pipe(
                tap(response => { }),
                catchError(error => of(console.log(error)))
            );
    }

    deleteMultipleUser(ids: []): Observable<any> {
        return this.http.put(this.urlUser, ids, httpOptions)
            .pipe(
                tap(response => { }),
                catchError(error => of(console.log(error)))
            );
    }

    restoreUser(id: any): Observable<any> {
        return this.http.put(`${this.urlRestoreUser}/${id}`, httpOptions).pipe(
            tap(response => { }),
            catchError(error => of(console.log(error)))
        );
    }

    //Behaviors
    getAllDataBehavior() {
        return this.http.get(this.urlBehavior);
    }

    deleteUserBehavior(id: any) {
        return this.http.delete(`${this.urlBehavior}/${id}`);
    }

    addUserBehavior(data: any) {
        return this.http.post(this.urlBehavior, data);
    }

    updateUserBehavior(id: any, data: any) {
        return this.http.put(`${this.urlBehavior}/${id}`, data);
    }
}
