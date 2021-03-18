import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ProfileComponent } from './profile/profile.component';
import { RegisterComponent } from './register/register.component';
import { AuthGuard } from './_helpers/auth.guard';
import { Role } from './_models/role';

const routes: Routes = [
    {
        path: '',
        component: HomeComponent
        // canActivate: [AuthGuard]
    },
    {
        path: 'admin',
        loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule),
        canActivate: [AuthGuard],
        //Khi active link => ActivatedRouteSnapshot chứa data roles=[Role.ADMIN]
        data: { roles: [Role.ADMIN] }
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: 'register',
        component: RegisterComponent
    },
    {
        path: 'profile',
        component: ProfileComponent
    },

    // otherwise redirect to home
    { path: '**', redirectTo: '' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes,{onSameUrlNavigation: 'reload',})],
    exports: [RouterModule]
})
export class AppRoutingModule { }
