import { AlertComponent } from './../alert/alert.component';
import { PlaceholderDirective } from './../alert/placeholder.directive';
import { Router } from '@angular/router';
import { ShoesDetailService } from './../service/shoes-detail.service';
import { DomSanitizer } from '@angular/platform-browser';
import { OneBillResponse } from './../model/one-bill-response.model';
import { ApiService } from './../service/api.service';
import { Component, OnInit, ViewChild, ComponentFactoryResolver } from '@angular/core';
import { Subscription } from 'rxjs';
import { formatDate } from '@angular/common';
declare var $: any;
@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.css']
})
export class CartComponent implements OnInit {
  cart!: OneBillResponse
  closeSub!: Subscription;
  @ViewChild(PlaceholderDirective) placeholderDirective!: PlaceholderDirective;
  constructor(private apiService: ApiService, private sanitizer: DomSanitizer,
    private shoesDetailService: ShoesDetailService, private router: Router,
    private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    this.apiService.getCart().subscribe(res => {
      this.cart = res.data;
    });
  }
  getImageURL(image: string){
    let objectURL = 'data:image/jpeg;base64,' + image;
    return this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }
  openCartModal(){
    $('#cartmodal').modal({backdrop: 'static', keyboard: false});
    $("#cartmodal").modal("show");
  }
  removeBillDetail(index: number){
    const billDetail = this.cart.listBillDetail[index];
    this.apiService.removeBillDetail(billDetail.id).subscribe(res => {
      this.cart.total = this.cart.total - billDetail.quantity * billDetail.price;
      this.shoesDetailService.removeQuantityCart(billDetail.quantity);
      this.cart.listBillDetail.splice(index, 1);
    });
  }
  editCart(event: any){
    if (this.cart.address != event.address){
      this.cart.address = event.address;
      this.apiService.editAddress(this.cart.address).subscribe();
    }
    if(this.cart.phone != event.phone){
      this.cart.phone = event.phone;
      this.apiService.editPhone(this.cart.phone).subscribe();
    }
  }
  payment(){
    this.apiService.payment().subscribe(res => {
      this.shoesDetailService.setQuantityCart(0);
      this.showErrorAlert(res.data, true);
    }, error => {
      this.showErrorAlert(error.error.message, false);
    })
  }
  onDetail(idShoe: number){
    this.router.navigate(['detail', idShoe]);
  }
  onSupermarket(){
    this.router.navigate(['shoes']);
  }
  private showErrorAlert(message: string, sucess: boolean){
    const alertCmpFactory = this.componentFactoryResolver.resolveComponentFactory(AlertComponent);
    const hostViewContainerRef = this.placeholderDirective.viewContainerRef;
    hostViewContainerRef.clear();
    const componentRef = hostViewContainerRef.createComponent(alertCmpFactory);
    componentRef.instance.message = message;
    this.closeSub = componentRef.instance.close.subscribe(() => {
      this.closeSub.unsubscribe();
      hostViewContainerRef.clear();
      if (sucess){
        this.router.navigate(['home']);
      }
    });
  }
}
