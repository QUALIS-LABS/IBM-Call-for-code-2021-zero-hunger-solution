import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MessagingComponent } from './dashboard/messaging/messaging.component'
import { TradersComponent } from './dashboard/traders/traders.component';

const routes: Routes = [
  {path:'', component: LoginComponent},
  {path: "login", component: LoginComponent},
  {path: "dashboard", component: DashboardComponent, 
  children: [
    {
        path: 'messaging',
        component: MessagingComponent
      },
    {
      path: 'traders',
      component: TradersComponent
    }
  ]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
