import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddUserComponent } from './add-user/add-user.component';
import { AdminComponent } from './admin.component';
import { ListUserComponent } from './list-user/list-user.component';
import { UpdateUserComponent } from './update-user/update-user.component';

const routes: Routes = [
    {
        path: 'addUser',
        component: AddUserComponent
    },
    {
        path: 'listUser',
        component: ListUserComponent,
        runGuardsAndResolvers: 'always'
    },
    {
        path: 'updateUser/:id',
        component: UpdateUserComponent
    },
    {
        path: '',
        component: AdminComponent
    },
    {
        path: '',
        redirectTo: '',
        pathMatch: 'full'
    }

];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdminRoutingModule { }
