import { BillDetailComponent } from './bill/bill-detail/bill-detail.component';
import { UserDetailComponent } from './user-detail/user-detail.component';
import { ShoesDetailComponent } from './shoes-detail/shoes-detail.component';
import { CartComponent } from './cart/cart.component';
import { ShoesComponent } from './shoes/shoes.component';
import { HomeComponent } from './home/home.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ShoesResolverService } from './service/shoes-resolver.service';
import { AuthComponent } from './auth/auth.component';
import { AuthGuard } from './auth/auth.guard';
import { BillComponent } from './bill/bill.component';

const routes: Routes = [
  {path: '', redirectTo: '/home', pathMatch: 'full', resolve: [ShoesResolverService]},
  {path: 'home', component: HomeComponent, resolve: [ShoesResolverService]},
  {path: 'shoes', component: ShoesComponent},
  {path: 'cart', component: CartComponent, canActivate: [AuthGuard]},
  {path: 'detail/:id', component: ShoesDetailComponent},
  {path: 'login', component: AuthComponent},
  {path: 'user-detail', component: UserDetailComponent, canActivate: [AuthGuard]},
  {path: 'bill', component: BillComponent, canActivate: [AuthGuard]},
  {path: 'bill-detail/:id', component: BillDetailComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { scrollPositionRestoration: 'enabled' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
