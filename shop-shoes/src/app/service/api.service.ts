import { BillSearchRequest } from './../model/bill-search-request.model';
import { BillUserResponse } from './../model/bill-user-reponse.model';
import { OneBillResponse } from './../model/one-bill-response.model';
import { CreateBillDetailRequest } from './../model/create-bill-detail-request.model';
import { ShoesDetailService } from './shoes-detail.service';
import { AuthService } from './auth.service';
import { LoginResponse } from './../model/login-response.model';
import { CreateUser } from './../model/create-user.model';
import { QuantityPerBrand } from './../model/quantity-per-brand.model';
import { BrandResponse } from './../model/brand-response.model';
import { ShoesPageRequest } from './../model/shoes-page-request.model';
import { ShoesResponse } from './../model/shoes-response.model';
import { BaseResponse } from './../model/base-response.model';
import { ShoesService } from './shoes.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from "@angular/core";
import { PageResponse } from '../model/page-response.model';
import { tap } from 'rxjs/operators';
import { OneShoeResponse } from '../model/one-shoe-response.model';
import { UpdateAvatar } from '../model/update-avatar.model';

@Injectable({providedIn: 'root'})
export class ApiService{
  constructor(private http: HttpClient, private shoesService: ShoesService, private authService: AuthService, private shoesDetailService: ShoesDetailService){}
  fetchShoes(){
    return this.http.post<BaseResponse<PageResponse<ShoesResponse[]>>>('http://localhost:8080/shoes/pages', new ShoesPageRequest(1, 19))
    .pipe(
      tap(response => {
        this.shoesService.onAddShoes(response.data);
      })
    );
  }
  fetchShoesListFilter(request: ShoesPageRequest){
    return this.http.post<BaseResponse<PageResponse<ShoesResponse[]>>>('http://localhost:8080/shoes/pages', request)
    .pipe(
      tap(response => {
        this.shoesService.onAddShoesList(response.data);
      })
    );
  }
  fetchBrands(){
    return this.http.get<BaseResponse<BrandResponse[]>>('http://localhost:8080/brand/list');
  }
  fetchQuantityPerBrand(){
    return this.http.get<BaseResponse<QuantityPerBrand[]>>('http://localhost:8080/shoes/quantityPerBrand');
  }
  fetchOneShoeDetail(id: number){
    return this.http.get<BaseResponse<OneShoeResponse>>('http://localhost:8080/shoes/'+id);
  }
  signUpUser(createUser: CreateUser){
    return this.http.post<BaseResponse<string>>('http://localhost:8080/consumer/signup', createUser);
  }
  signInUser(username: string, password: string, autoLogin: boolean){
    let params = new HttpParams()
    .set('username', username)
    .set('password', password);

    return this.http.get<BaseResponse<LoginResponse>>('http://localhost:8080/consumer/signin', {params: params})
    .pipe(
      tap(response => {
        this.authService.login(autoLogin, response.data);
        this.getQuantityCart().subscribe();
      })
    );
  }
  getQuantityCart(){
    return this.http.get<BaseResponse<number>>('http://localhost:8080/bill/quantityCart').pipe(
      tap(response => {
        this.shoesDetailService.setQuantityCart(response.data);
      })
    );
  }
  addBillDetail(createBillDetailRequest: CreateBillDetailRequest){
    return this.http.post<BaseResponse<string>>('http://localhost:8080/bill/addCart', createBillDetailRequest);
  }
  getCart(){
    return this.http.get<BaseResponse<OneBillResponse>>('http://localhost:8080/bill/cart');
  }
  changePassword(oldPassword: string, newPassword: string){
    let params = new HttpParams()
    .set('oldPassword', oldPassword)
    .set('newPassword', newPassword);
    return this.http.get<BaseResponse<string>>('http://localhost:8080/consumer/changePassword', {params: params});
  }
  removeBillDetail(id: number){
    return this.http.get<BaseResponse<string>>('http://localhost:8080/bill/removeBillDetail/'+id);
  }
  payment(){
    return this.http.get<BaseResponse<string>>('http://localhost:8080/bill/payment');
  }
  editAddress(address: string){
    let params = new HttpParams()
    .set('address', address);
    return this.http.get<BaseResponse<string>>('http://localhost:8080/bill/editAddress', {params: params});
  }
  editPhone(phone: string){
    let params = new HttpParams()
    .set('phone', phone);
    return this.http.get<BaseResponse<string>>('http://localhost:8080/bill/editPhone', {params: params});
  }
  getHistoryBill(billSearchRequest: BillSearchRequest){
    return this.http.post<BaseResponse<PageResponse<BillUserResponse[]>>>('http://localhost:8080/bill/historyBill', billSearchRequest);
  }
  updateAvatar(image: string){
    const object = new UpdateAvatar(image);
    return this.http.post<BaseResponse<string>>('http://localhost:8080/consumer/updateAvatar', object).pipe(
      tap(res => {
        this.authService.updateAvatar(image);
      })
    );
  }
  updateInfoUser(infoUser: {firstName: string, lastName: string, email: string, phone: string, address: string}){
    return this.http.post<BaseResponse<string>>('http://localhost:8080/consumer/updateInfoUser', infoUser).pipe(
      tap(res => {
        this.authService.updateInfoUser(infoUser);
      })
    );
  }
  getOneBill(id: number){
    return this.http.get<BaseResponse<OneBillResponse>>('http://localhost:8080/bill/'+id);
  }
}

// export class SourceApi {
//   BASE_API = "http://localhost:8080/";
//   PAGE_SHOES = BASE_API + "asd";
// }
