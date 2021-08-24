import { AuthInterceptorService } from './service/auth-interceptor.service';
import { PlaceholderDirective } from './alert/placeholder.directive';
import { AlertComponent } from './alert/alert.component';
import { ShoesService } from './service/shoes.service';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import {CarouselModule} from 'primeng/carousel';
import { ButtonModule } from 'primeng/button';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { HomeComponent } from './home/home.component';
import { FooterComponent } from './footer/footer.component';
import { CarouselSliderComponent } from './carousel-slider/carousel-slider.component';
import { ShoesComponent } from './shoes/shoes.component';
import { CartComponent } from './cart/cart.component';
import { ShoesDetailComponent } from './shoes-detail/shoes-detail.component';
import { ListViewComponent } from './shoes/list-view/list-view.component';
import { GridViewComponent } from './shoes/grid-view/grid-view.component';
import { AuthComponent } from './auth/auth.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ModalCartComponent } from './cart/modal-cart/modal-cart.component';
import { ChangePasswordComponent } from './auth/change-password/change-password.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { BillComponent } from './bill/bill.component';
import { BillDetailComponent } from './bill/bill-detail/bill-detail.component';


@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HomeComponent,
    FooterComponent,
    CarouselSliderComponent,
    ShoesComponent,
    CartComponent,
    ShoesDetailComponent,
    ListViewComponent,
    GridViewComponent,
    AuthComponent,
    AlertComponent,
    PlaceholderDirective,
    ModalCartComponent,
    ChangePasswordComponent,
    UserDetailComponent,
    BillComponent,
    BillDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    CarouselModule,
    ButtonModule,
    ReactiveFormsModule
  ],
  providers: [ShoesService, {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}],
  bootstrap: [AppComponent],
  entryComponents: [AlertComponent]
})
export class AppModule { }
