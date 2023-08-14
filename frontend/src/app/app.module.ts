import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { InputTextModule } from 'primeng/inputtext';
import { AvatarModule } from 'primeng/avatar';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import { SidebarModule } from 'primeng/sidebar';
import { MessageModule } from 'primeng/message';
import { CardModule } from 'primeng/card';
import { BadgeModule } from 'primeng/badge';

import { CustomerComponent } from './components/customer/customer.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { SidebarItemComponent } from './components/sidebar-item/sidebar-item.component';
import { HeaderComponent } from './components/header/header.component';
import { ManageCustomerComponent } from './components/manage-customer/manage-customer.component';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { HttpInterceptorService } from './services/interceptor/http-interceptor.service';
import { CustomerCardComponent } from './components/customer-card/customer-card.component';

@NgModule({
  declarations: [
    AppComponent,
    CustomerComponent,
    SidebarComponent,
    SidebarItemComponent,
    HeaderComponent,
    ManageCustomerComponent,
    LoginComponent,
    CustomerCardComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    InputTextModule,
    AvatarModule,
    ButtonModule,
    MenuModule,
    BrowserModule,
    BrowserAnimationsModule,
    SidebarModule,
    FormsModule,
    HttpClientModule,
    MessageModule,
    CardModule,
    BadgeModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
