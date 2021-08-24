import { ShoesResponse } from './../../model/shoes-response.model';
import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-list-view',
  templateUrl: './list-view.component.html',
  styleUrls: ['./list-view.component.css']
})
export class ListViewComponent implements OnInit {

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
