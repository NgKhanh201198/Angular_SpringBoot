import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {BehaviorSubject, Observable, throwError} from 'rxjs';
import {CurrentUser} from '../_models/current-user';
import {catchError, map} from 'rxjs/operators';
import {environment} from 'src/environments/environment';

const AUTH_API = `${environment.url}` + 'api/auth/';
const TOKEN_KEY = 'TOKEN';
const USER_KEY = 'USER';
const httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
};

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<CurrentUser>;
    public currentUser: Observable<CurrentUser>;

    constructor(private http: HttpClient, private router: Router) {
        this.currentUserSubject = new BehaviorSubject<CurrentUser>(JSON.parse(window.sessionStorage.getItem(USER_KEY)));
        this.currentUser = this.currentUserSubject.asObservable();
    }

    public storeToken(token: string): void {
        window.sessionStorage.removeItem(TOKEN_KEY);
        window.sessionStorage.setItem(TOKEN_KEY, token);
    }

    public getToken(): string {
        return window.sessionStorage.getItem(TOKEN_KEY);
    }

    public storeUser(user: string): void {
        window.sessionStorage.removeItem(USER_KEY);
        window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    }

    public getUser(): any {
        return JSON.parse(window.sessionStorage.getItem(USER_KEY));
    }

    public get currentUserValue(): CurrentUser {
        return this.currentUserSubject.value;
    }

    login(username: any, password: any): Observable<any> {
        return this.http.post(AUTH_API + 'login', {username, password}, httpOptions)
            .pipe(
                map((user: any) => {
                    this.storeToken(user.token);
                    this.storeUser(user);
                    this.currentUserSubject.next(user);
                    return user;
                }),
                catchError((error) => {
                    return throwError(error);
                })
            );
    }

    logout(): void {
        window.sessionStorage.clear();
        this.currentUserSubject.next(null);
    }

    isLoggedIn(): boolean {
        return !!this.getToken();
    }
}
