import { Injectable } from "@angular/core";
import { Subject } from "rxjs";

@Injectable({providedIn: 'root'})
export class ShoesDetailService{
  private quantityCart = 0;
  quantityCartSub = new Subject<number>();
  setQuantityCart(quantity: number){
    this.quantityCart = quantity;
    this.quantityCartSub.next(this.quantityCart);
  }
  addQuantityCart(quantity: number){
    this.quantityCart += quantity;
    this.quantityCartSub.next(this.quantityCart);
  }
  removeQuantityCart(quantity: number){
    if (this.quantityCart - quantity >= 0){
      this.quantityCart -= quantity;
      this.quantityCartSub.next(this.quantityCart);
    }
  }
}
