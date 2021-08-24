import { ShoesDetailService } from './../service/shoes-detail.service';
import { AuthService } from './../service/auth.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ApiService } from './../service/api.service';
import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';
import { OneShoeResponse } from '../model/one-shoe-response.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CreateBillDetailRequest } from '../model/create-bill-detail-request.model';
declare var $: any;

@Component({
  selector: 'app-shoes-detail',
  templateUrl: './shoes-detail.component.html',
  styleUrls: ['./shoes-detail.component.css']
})
export class ShoesDetailComponent implements OnInit {
  shoe!: OneShoeResponse;
  selectedIdShoeDetail = -1;
  id!: number;
  selectImageIndex = 0;
  isLogin = false;
  addCartSuccess = false;
  constructor(private apiService: ApiService,
    private route: ActivatedRoute, private router: Router, private sanitizer: DomSanitizer, private authService: AuthService,
    private shoesDetailService: ShoesDetailService) { }

  ngOnInit(): void {
    this.authService.user.subscribe(res => {
      this.isLogin = !!res;
    })
    this.route.params.subscribe(params => {
      this.id = +params['id'];
      this.apiService.fetchOneShoeDetail(this.id).subscribe(res => {
        console.log(res.data);
        this.shoe = res.data;
      }, error => {
        this.router.navigate(['shoes']);
      });
    });
  }
  getImageURL(image: string){
    let objectURL = 'data:image/jpeg;base64,' + image;
    return this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }
  buyNow(){
    if (this.isLogin){
      if (this.selectedIdShoeDetail != -1 && this.selectedIdShoeDetail != 0){
        this.shoesDetailService.addQuantityCart(+this.quantityShoes.nativeElement.value);
        this.apiService.addBillDetail(new CreateBillDetailRequest(this.selectedIdShoeDetail, +this.quantityShoes.nativeElement.value, this.shoe.discount)).subscribe(res => {
          this.router.navigate(['cart']);
        });
      } else {
        this.selectedIdShoeDetail = 0;
      }
    } else {
      $('#loginmodal').modal({backdrop: 'static', keyboard: false})
      $("#loginmodal").modal("show");
    }
  }
  addToCart(){
    if (this.isLogin){
      if (this.selectedIdShoeDetail != -1 && this.selectedIdShoeDetail != 0){
        this.shoesDetailService.addQuantityCart(+this.quantityShoes.nativeElement.value);
        this.apiService.addBillDetail(new CreateBillDetailRequest(this.selectedIdShoeDetail, +this.quantityShoes.nativeElement.value, this.shoe.discount)).subscribe(res => {
          this.addCartSuccess = true;
          setTimeout(() => {
            this.addCartSuccess = false;
          }, 1500)
        });
      } else {
        this.selectedIdShoeDetail = 0;
      }
    } else {
      $('#loginmodal').modal({backdrop: 'static', keyboard: false})
      $("#loginmodal").modal("show");
    }
  }
  selectShoeDetail(id: number){
    this.selectedIdShoeDetail = id;
  }
  @ViewChild('quantityShoes') quantityShoes!: ElementRef;
  changeQuantity(){
    if (this.quantityShoes.nativeElement.value){
      if (+this.quantityShoes.nativeElement.value <1){
        this.quantityShoes.nativeElement.value = 1;
      }
      if (+this.quantityShoes.nativeElement.value > 10){
        this.quantityShoes.nativeElement.value = 10;
      }
    }

  }
  changeImageIndex(index: number){
    this.selectImageIndex = index;
  }
}
