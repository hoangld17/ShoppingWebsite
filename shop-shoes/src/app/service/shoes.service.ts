import { ShoesResponse } from './../model/shoes-response.model';
import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { PageResponse } from "../model/page-response.model";

@Injectable()
export class ShoesService{
  shoesChanged = new Subject<PageResponse<ShoesResponse[]>>();
  private shoes: PageResponse<ShoesResponse[]> | null = null;
  onAddShoes(shoes: PageResponse<ShoesResponse[]>){
    this.shoes = shoes;
    this.shoesChanged.next(this.shoes);
  }
  get getShoes(){
    return this.shoes;
  }
  shoesListChanged = new Subject<PageResponse<ShoesResponse[]>>();
  private shoesList: PageResponse<ShoesResponse[]> | null = null;
  onAddShoesList(shoes: PageResponse<ShoesResponse[]>){
    this.shoesList = shoes;
    this.shoesListChanged.next(this.shoesList);
  }
  get getShoesList(){
    return this.shoesList;
  }
}
