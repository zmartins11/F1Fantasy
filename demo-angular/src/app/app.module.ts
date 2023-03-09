import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { EmployeeListComponent } from './components/employee-list/employee-list.component';
import { CreateEmployeeComponent } from './components/create-employee/create-employee.component';
import { UpdateEmployeeComponent } from './components/update-employee/update-employee.component';
import { DriversListComponent } from './components/drivers-list/drivers-list.component';
import { NgxPaginationModule } from 'ngx-pagination';

import { MatInputModule } from '@angular/material/input';
import { FlexLayoutModule } from '@angular/flex-layout';

import { TestCssComponent } from './components/test-css/test-css.component';

import { Ng2OrderModule } from 'ng2-order-pipe';
import { Ng2SearchPipeModule } from 'ng2-search-filter';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CalendarComponent } from './components/calendar/calendar.component';
import { RaceDetailsComponent } from './components/race-details/race-details.component';
import { ModalModule } from 'ngx-bootstrap/modal';
import { BsModalService } from 'ngx-bootstrap/modal';
import { RaceResultsComponent } from './components/race-results/race-results.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { FantasyHomeComponent } from './components/fantasy-home/fantasy-home.component';
import { FantasyTeamComponent } from './components/fantasy-team/fantasy-team.component';





@NgModule({
  declarations: [
    AppComponent,
    EmployeeListComponent,
    CreateEmployeeComponent,
    UpdateEmployeeComponent,
    DriversListComponent,
    TestCssComponent,
    CalendarComponent,
    RaceDetailsComponent,
    RaceResultsComponent,
    LoginComponent,
    RegisterComponent,
    FantasyHomeComponent,
    FantasyTeamComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    NgxPaginationModule,
    MatInputModule,
    FlexLayoutModule,
    Ng2SearchPipeModule,
    NgbModule,
    ModalModule
  ],
  providers: [
    BsModalService 
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
