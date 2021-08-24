import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { ShoesDetailService } from './../service/shoes-detail.service';
import { ApiService } from './../service/api.service';
import { AuthService } from './../service/auth.service';
import { Component, OnInit } from '@angular/core';
import { LoginResponse } from '../model/login-response.model';

declare var $: any;

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  loginResponse: LoginResponse | null = null;
  quantityCart = 0;
  constructor(private authService: AuthService, private apiService: ApiService,
    private shoesDetailService: ShoesDetailService, private router: Router,
    private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    this.authService.user.subscribe(value => {
      this.loginResponse = value;
      if(this.loginResponse){
        this.shoesDetailService.quantityCartSub.subscribe(res => {
          this.quantityCart = res;
        })
      }
    });
  }
  transferImage(image: String){
    let objectURL = 'data:image/jpeg;base64,' + image;
    return this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }
  openLogin(){
    $('#loginmodal').modal({backdrop: 'static', keyboard: false})
    $("#loginmodal").modal("show");
  }
  openRegister(){
    $('#registermodal').modal({backdrop: 'static', keyboard: false})
    $("#registermodal").modal("show");

  }
  logout(){
    this.authService.logout();
    window.location.reload();
  }
  openCart(){
    if(this.loginResponse){
      this.router.navigate(['cart']);
    } else {
      this.openLogin();
    }
  }
  openInfoUser(){
    this.router.navigate(['user-detail']);
  }
  openChangePassword(){
    $('#changepasswordmodal').modal({backdrop: 'static', keyboard: false});
    $('#changepasswordmodal').modal('show');
  }

  openBill(){
    this.router.navigate(['bill']);
  }
}
