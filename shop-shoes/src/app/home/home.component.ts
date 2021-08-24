import { Router } from '@angular/router';
import { ApiService } from './../service/api.service';
import { ShoesResponse } from './../model/shoes-response.model';
import { ShoesService } from './../service/shoes.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {
  products = ['assets/images/1.jpg', 'assets/images/1.jpg', 'assets/images/1.jpg'];
  subs!: Subscription;
  shoes!: ShoesResponse[];
  image: any[] = [];
  constructor(private shoesService: ShoesService, private sanitizer: DomSanitizer, private apiService: ApiService, private router: Router) { }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  ngOnInit(): void {
    this.apiService.fetchShoes().subscribe();
    this.subs = this.shoesService.shoesChanged.subscribe(res => {
      this.shoes = res.content;
      this.shoes.forEach(x => {
        let objectURL = 'data:image/jpeg;base64,' + x.image;
        this.image.push(this.sanitizer.bypassSecurityTrustUrl(objectURL));
      });
    });
  }
  getProductsCarousel(){
    if (this.shoes.slice(7) != undefined)
      return this.shoes.slice(7);
    return [];
  }
  getImageCarousel(){
    if (this.image.slice(7) != undefined)
      return this.image.slice(7);
    return [];
  }
  onDetail(id: number){
    this.router.navigate(['detail', id]);
  }
}
