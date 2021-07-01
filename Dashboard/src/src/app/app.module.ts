import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from "./auth/login/login.component";
import { DashboardComponent } from "./dashboard/dashboard.component";
import { MessagingComponent } from "./dashboard/messaging/messaging.component";
import { TradersComponent } from "./dashboard/traders/traders.component"

import { MatCardModule} from '@angular/material/card';
import { FormsModule } from '@angular/forms';
import { MatInputModule} from '@angular/material/input';
import { MatButtonModule} from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatExpansionModule} from '@angular/material/expansion';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatIconModule} from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import {MatTableModule} from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    DashboardComponent,
    MessagingComponent,
    TradersComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    MatButtonModule,
    MatCardModule,
    FormsModule,
    MatInputModule,
    BrowserAnimationsModule,
    MatProgressSpinnerModule,
    MatExpansionModule,
    MatToolbarModule,
    MatSidenavModule,
    MatIconModule,
    MatMenuModule,
    MatButtonToggleModule,
    MatTableModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
