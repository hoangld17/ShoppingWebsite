import { Router } from '@angular/router';
import { ShoesResponse } from './../model/shoes-response.model';
import { Component, Input, OnInit, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-carousel-slider',
  templateUrl: './carousel-slider.component.html',
  styleUrls: ['./carousel-slider.component.css']
})
export class CarouselSliderComponent implements OnInit {


  ngOnInit(): void {
  }
   //slider setting variable
   responsiveOptions = [
    {
        breakpoint: '1024px',
        numVisible: 3,
        numScroll: 3
    },
    {
        breakpoint: '768px',
        numVisible: 2,
        numScroll: 2
    },
    {
        breakpoint: '560px',
        numVisible: 1,
        numScroll: 1
    }
];
   //define validable to store dynamic products data
   @Input() products: ShoesResponse[] = [];
   @Input() images: any
   constructor(private router: Router){}
  index(product: any){
   return this.products!.findIndex((x: any) => x == product);;
 }
  onDetail(id: number){
    this.router.navigate(['detail', id]);
  }
}
