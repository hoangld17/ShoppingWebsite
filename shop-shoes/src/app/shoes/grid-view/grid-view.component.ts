import { Router } from '@angular/router';
import { ShoesResponse } from './../../model/shoes-response.model';
import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-grid-view',
  templateUrl: './grid-view.component.html',
  styleUrls: ['./grid-view.component.css']
})
export class GridViewComponent implements OnInit {
  @Input() shoes!: ShoesResponse[];
  @Input() images!: any;
  constructor(private router: Router) { }

  ngOnInit(): void {
  }
  isDiscount(index: number) {
    return this.shoes[index].discount != this.shoes[index].price;
  }
  onDetail(index: number){
    this.router.navigate(['detail', index]);
  }
}
