import { Router } from '@angular/router';
import { BrandResponse } from './../model/brand-response.model';
import { ApiService } from './../service/api.service';
import { DomSanitizer } from '@angular/platform-browser';
import { ShoesService } from './../service/shoes.service';
import { ShoesResponse } from './../model/shoes-response.model';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { PageResponse } from '../model/page-response.model';
import { ShoesPageRequest } from '../model/shoes-page-request.model';

@Component({
  selector: 'app-shoes',
  templateUrl: './shoes.component.html',
  styleUrls: ['./shoes.component.css']
})
export class ShoesComponent implements OnInit {
  isListView = true;
  subs!: Subscription;
  page!: PageResponse<ShoesResponse[]>;
  image: any[] = [];
  paging: boolean[] | null = null;
  indexPage = 0;
  totalPages!: number;
  totalResult!: number;
  brands!: BrandResponse[];
  canSearchBrand = false;
  requestFilter = new ShoesPageRequest(1, 6);
  sortField = "latest";
  selectedIdBrands: number[] = [];
  @ViewChild('name') name!: ElementRef;
  constructor(private shoesService: ShoesService, private sanitizer: DomSanitizer, private apiService: ApiService, private router: Router) { }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  ngOnInit(): void {
    this.apiService.fetchBrands().subscribe(res => {
      this.brands = res.data;
      this.brands.forEach(x => {
        this.selectedIdBrands.push(x.id);
      });
      this.apiService.fetchQuantityPerBrand().subscribe(other => {
        other.data.forEach(x => {
          this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
        });
      })

    });
    this.apiService.fetchShoesListFilter(this.requestFilter).subscribe();
    this.subs = this.shoesService.shoesListChanged.subscribe(res => {
      this.page = res;
      if (!this.paging){
        this.totalResult = this.page.totalElements;
        this.paging = new Array(this.page.totalPages).fill(false);
        this.totalPages = this.page.totalPages;
        this.paging[0] = true;
        this.indexPage = 0;
      }
      this.image = [];
      this.page.content.forEach(x => {
        let objectURL = 'data:image/jpeg;base64,' + x.image;
        this.image.push(this.sanitizer.bypassSecurityTrustUrl(objectURL));
      });
    });
  }
  onChangeMode(){
    this.isListView = !this.isListView;
  }
  onChangePage(index: number){
    if (index != this.indexPage) {
      this.paging![this.indexPage] = false;
      this.paging![index] = true;
      this.indexPage = index;
      this.requestFilter.page = this.indexPage + 1;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe();
    }
  }
  onNextPage(){
    if (this.isNext){
      this.paging![this.indexPage] = false;
      this.paging![this.indexPage + 1] = true;
      this.indexPage += 1;
      this.requestFilter.page = this.indexPage + 1;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe();
    }
  }
  onPreviousPage(){
    if (this.isPrevious){
      this.paging![this.indexPage] = false;
      this.paging![this.indexPage - 1] = true;
      this.indexPage -= 1;
      this.requestFilter.page = this.indexPage + 1;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe();
    }
  }
  get isNext(){
    return !(this.indexPage == this.totalPages - 1);
  }
  get isPrevious(){
    return !(this.indexPage == 0);
  }
  searchName(){
    if (this.name.nativeElement.value && this.name.nativeElement.value.trim != ''){
      this.requestFilter = new ShoesPageRequest(1, 6);
      if (this.sortField == "cheapest") {
        this.requestFilter.sortType = true;
        this.requestFilter.sortField = "discountPrice";
      } else if (this.sortField == "highest") {
        this.requestFilter.sortType = false;
        this.requestFilter.sortField = "discountPrice";
      }
      this.requestFilter.search = this.name.nativeElement.value;
      this.paging = null;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
        this.brands.forEach(x => x.quantity = 0);
        this.apiService.fetchQuantityPerBrand().subscribe(other => {
         other.data.forEach(x => {
            this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
         });
        })
      });
    } else if (this.page.content.length == 0){
      this.paging = null;
      this.requestFilter = new ShoesPageRequest(1, 6);
      if (this.sortField == "cheapest") {
        this.requestFilter.sortType = true;
        this.requestFilter.sortField = "discountPrice";
      } else if (this.sortField == "highest") {
        this.requestFilter.sortType = false;
        this.requestFilter.sortField = "discountPrice";
      }
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
        this.brands.forEach(x => x.quantity = 0);
        this.apiService.fetchQuantityPerBrand().subscribe(other => {
         other.data.forEach(x => {
            this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
         });
        })
      });
    }
  }

  OnCheckboxSelect(id: number, event: any) {
    if (event.target.checked === true) {
      this.selectedIdBrands.push(id);
    }
    if (event.target.checked === false) {
      this.selectedIdBrands = this.selectedIdBrands.filter((item) => item !== id);
    }
  }
  searchBrand(){
    if (this.selectedIdBrands.length != 0 && (this.canSearchBrand || this.selectedIdBrands.length != this.brands.length)){
      this.canSearchBrand = true;
      if (this.selectedIdBrands.length == this.brands.length){
        this.canSearchBrand = false;
      }
      this.requestFilter = new ShoesPageRequest(1, 6);
      if (this.sortField == "cheapest") {
        this.requestFilter.sortType = true;
        this.requestFilter.sortField = "discountPrice";
      } else if (this.sortField == "highest") {
        this.requestFilter.sortType = false;
        this.requestFilter.sortField = "discountPrice";
      }
      this.requestFilter.idBrands = this.selectedIdBrands;
      this.paging = null;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
        this.brands.forEach(x => x.quantity = 0);
        this.apiService.fetchQuantityPerBrand().subscribe(other => {
         other.data.forEach(x => {
            this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
         });
        })
      });
    }
  }
  @ViewChild('minPrice') minPrice!: ElementRef;
  @ViewChild('maxPrice') maxPrice!: ElementRef;
  changeMin(){
    if (this.minPrice.nativeElement.value){
      if (+this.minPrice.nativeElement.value <0){
        this.minPrice.nativeElement.value = 0;
      }
      if (this.maxPrice.nativeElement.value && +this.minPrice.nativeElement.value > +this.maxPrice.nativeElement.value) {
        this.minPrice.nativeElement.value = +this.maxPrice.nativeElement.value;
      } else if (+this.minPrice.nativeElement.value > 10000){
        this.minPrice.nativeElement.value = 10000;
      }
    }

  }
  changeMax(){
    if (this.maxPrice.nativeElement.value){
      if (+this.maxPrice.nativeElement.value > 10000){
        this.maxPrice.nativeElement.value = 10000;
      }
      if (this.minPrice.nativeElement.value && +this.minPrice.nativeElement.value > +this.maxPrice.nativeElement.value) {
        this.maxPrice.nativeElement.value = +this.minPrice.nativeElement.value;
      } else if (+this.minPrice.nativeElement.value < 0){
        this.maxPrice.nativeElement.value = 0;
      }
    }
  }
  searchPriceRange(){
    if (this.minPrice.nativeElement.value && this.maxPrice.nativeElement.value){
      this.requestFilter = new ShoesPageRequest(1, 6);
      if (this.sortField == "cheapest") {
        this.requestFilter.sortType = true;
        this.requestFilter.sortField = "discountPrice";
      } else if (this.sortField == "highest") {
        this.requestFilter.sortType = false;
        this.requestFilter.sortField = "discountPrice";
      }
      this.requestFilter.minPrice = +this.minPrice.nativeElement.value;
      this.requestFilter.maxPrice = +this.maxPrice.nativeElement.value;
      this.paging = null;
      this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
        this.brands.forEach(x => x.quantity = 0);
        this.apiService.fetchQuantityPerBrand().subscribe(other => {
         other.data.forEach(x => {
            this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
         });
        })
      });
    }
  }
  searchAll(){
    if (this.name.nativeElement.value || this.minPrice.nativeElement.value && this.maxPrice.nativeElement.value || (this.selectedIdBrands.length != 0 && (this.canSearchBrand || this.selectedIdBrands.length != this.brands.length))){
      this.requestFilter = new ShoesPageRequest(1, 6);
    if(this.name.nativeElement.value){
      this.requestFilter.search = this.name.nativeElement.value;
    }
    if (this.minPrice.nativeElement.value && this.maxPrice.nativeElement.value){
      this.requestFilter.minPrice = +this.minPrice.nativeElement.value;
      this.requestFilter.maxPrice = +this.maxPrice.nativeElement.value;
    }
    if (this.selectedIdBrands.length != 0){
      this.canSearchBrand = true;
      if (this.selectedIdBrands.length == this.brands.length){
        this.canSearchBrand = false;
      }
      this.requestFilter.idBrands = this.selectedIdBrands;
    }
    if (this.sortField == "cheapest") {
      this.requestFilter.sortType = true;
      this.requestFilter.sortField = "discountPrice";
    } else if (this.sortField == "highest") {
      this.requestFilter.sortType = false;
      this.requestFilter.sortField = "discountPrice";
    }
    this.paging = null;
    this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
      this.brands.forEach(x => x.quantity = 0);
      this.apiService.fetchQuantityPerBrand().subscribe(other => {
       other.data.forEach(x => {
          this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
       });
      })
    });
    }
  }
  modo(event: any){
    switch(event.target.value) {
      case "latest":
        if (this.sortField == "latest"){
          break;
        }
        this.requestFilter = new ShoesPageRequest(1, 6);
        this.paging = null;
        this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
          this.brands.forEach(x => x.quantity = 0);
          this.apiService.fetchQuantityPerBrand().subscribe(other => {
           other.data.forEach(x => {
              this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
           });
          })
        });
         break;
      case "cheapest":
        if (this.sortField == "cheapest"){
          break;
        }
        this.sortField = "cheapest";
        this.paging = null;
        this.requestFilter.page = 1;
        this.requestFilter.sortType = true;
        this.requestFilter.sortField = "discountPrice";
        this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
          this.brands.forEach(x => x.quantity = 0);
          this.apiService.fetchQuantityPerBrand().subscribe(other => {
           other.data.forEach(x => {
              this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
           });
          })
        });
         break;
      case "highest":
        if (this.sortField == "highest"){
          break;
        }
        this.sortField = "highest";
        this.paging = null;
        this.requestFilter.page = 1;
        this.requestFilter.sortType = false;
        this.requestFilter.sortField = "discountPrice";
        this.apiService.fetchShoesListFilter(this.requestFilter).subscribe(res => {
          this.brands.forEach(x => x.quantity = 0);
          this.apiService.fetchQuantityPerBrand().subscribe(other => {
           other.data.forEach(x => {
              this.brands.find(y => x.id == y.id)!.quantity = x.quantity;
           });
          })
        });
         break;
    }
  }
}
