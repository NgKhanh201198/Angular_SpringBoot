import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { Observable, of, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';
import { LogService } from './log.service';

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

    constructor(
        private http: HttpClient
    ) { }

    handleError(error: HttpErrorResponse) {
        return throwError(error);
    }

    // User
    getAllData(): Observable<any> {
        return this.http.get(this.urlUser).pipe(
            tap(response => { }),
            catchError(error => of(console.log(error)))
        );
    }
    getDataByID(id: number): Observable<any> {
        return this.http.get(`${this.urlUser}/${id}`).pipe(
            catchError(this.handleError)
        );
    }

    addUser(data: any): Observable<any> {
        return this.http.post(this.urlAddUser, data, httpOptions)
            .pipe(
                catchError(this.handleError)
            );
    }

    updateUser(id: any, data: any): Observable<any> {
        return this.http.put(`${this.urlUser}/${id}`, data, httpOptions)
            .pipe(
                catchError(this.handleError)
            );
    }

    deleteUserById(id: any): Observable<any> {
        return this.http.delete(`${this.urlUser}/${id}`)
            .pipe(
                catchError(this.handleError)
            );
    }

    deleteMultipleUser(ids: []): Observable<any> {
        return this.http.put(this.urlUser, ids, httpOptions)
            .pipe(
                catchError(this.handleError)
            );
    }

    restoreUser(id: any): Observable<any> {
        return this.http.put(`${this.urlRestoreUser}/${id}`, httpOptions).pipe(
            catchError(this.handleError)
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