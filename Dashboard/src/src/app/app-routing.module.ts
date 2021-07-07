import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { MessagingComponent } from './dashboard/messaging/messaging.component'
import { TradersComponent } from './dashboard/traders/traders.component';
import { FarmersComponent } from './dashboard/farmers/farmers.component';
import { DriversComponent } from './dashboard/drivers/drivers.component';
import { OverviewComponent } from './dashboard/overview/overview.component';

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
    },
    {
      path: 'farmers',
      component: FarmersComponent
    },
    {
      path: 'drivers',
      component: DriversComponent
    },
    {
      path: 'overview',
      component: OverviewComponent
    }
  ]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
