import { Router, ActivatedRoute } from '@angular/router';
import { BillSearchRequest } from './../model/bill-search-request.model';
import { ApiService } from './../service/api.service';
import { BillUserResponse } from './../model/bill-user-reponse.model';
import { PageResponse } from './../model/page-response.model';
import { formatDate } from '@angular/common';
import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-bill',
  templateUrl: './bill.component.html',
  styleUrls: ['./bill.component.css']
})
export class BillComponent implements OnInit {
  bills!: BillUserResponse[];
  paging: boolean[] | null = null;
  indexPage!: number;
  totalPages!: number;
  totalResult!: number;
  billFilter!: BillSearchRequest;
  @ViewChild('fromDate') fromDate!: ElementRef;
  @ViewChild('toDate') toDate!: ElementRef;
  today = new Date();
  formatPurchaseDate(date: Date){
    return formatDate(date, 'MM/dd/yyyy HH:mm', 'en-US')
  }
  convertDate(date: Date, isToDate?: boolean){
    if (isToDate) {
      return date.toString() == '' ? formatDate(this.today, 'yyyy-MM-dd', 'en-US') : formatDate(date, 'yyyy-MM-dd', 'en-US');
    }
    return date.toString() == '' ? '' : formatDate(date, 'yyyy-MM-dd', 'en-US');
  }
  get fromDateValue(){
    if (this.fromDate != undefined) {
      return this.convertDate(this.fromDate.nativeElement.value, false);
    }
    return '';
  }
  get toDateValue(){
    if (this.toDate != undefined) {
      return this.convertDate(this.toDate.nativeElement.value, true);
    }
    return this.convertDate(this.today, true);
  }
  constructor(private apiService: ApiService, private router: Router, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.billFilter = new BillSearchRequest(true, 0);
    this.updateData();
  }
  private updateData(){
    this.apiService.getHistoryBill(this.billFilter).subscribe(res => {
      const data = res.data;
      this.bills = data.content;
      this.totalPages = data.totalPages;
      this.totalResult = data.totalElements;
      if (!this.paging || this.paging.length != this.totalPages){
        this.paging = new Array(this.totalPages).fill(false);
      } else {
        this.paging[this.indexPage] = false;
      }
      this.indexPage = data.number;
      this.paging[this.indexPage] = true;
    })
  }
  onChangePage(index: number){
    if (index != this.indexPage) {
      this.billFilter.page = index;
      this.updateData();
    }
  }
  onNextPage(){
    if (this.isNext){
      this.billFilter.page += 1;
      this.updateData();
    }
  }
  onPreviousPage(){
    if (this.isPrevious){
      this.billFilter.page -= 1;
      this.updateData();
    }
  }
  get isNext(){
    return !(this.indexPage == this.totalPages - 1);
  }
  get isPrevious(){
    return !(this.indexPage == 0);
  }
  modo(event: any){
    switch(event.target.value) {
      case "latest":
        if (!this.billFilter.latest) {
          this.billFilter.latest = true;
          this.updateData();
        }
        break;
      case "cheapest":
        if (this.billFilter.latest) {
          this.billFilter.latest = false;
          this.updateData();
        }
        break;
    }
  }
  navigateBillDetail(id: number) {
    this.router.navigate(['bill-detail', id]);
  }
  searchDate(){
    if (this.billFilter.fromDate != this.fromDateValue || this.billFilter.toDate != this.toDateValue){

      if (this.fromDateValue != '') {
        this.billFilter.fromDate = this.fromDateValue;
      }
      this.billFilter.toDate = this.toDateValue
      console.log(this.billFilter.fromDate);
      console.log(this.billFilter.toDate);
      this.updateData();
    }
  }
}
