import { ApiService } from './../../service/api.service';
import { DomSanitizer } from '@angular/platform-browser';
import { OneBillResponse } from './../../model/one-bill-response.model';
import { ActivatedRoute, Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-bill-detail',
  templateUrl: './bill-detail.component.html',
  styleUrls: ['./bill-detail.component.css']
})
export class BillDetailComponent implements OnInit {
  idBill!: number;
  bill!: OneBillResponse;
  constructor(private route: ActivatedRoute, private router: Router,
    private sanitizer: DomSanitizer, private apiService: ApiService) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.idBill = +params['id'];
      this.apiService.getOneBill(this.idBill).subscribe(res => {
        this.bill = res.data;
      }, error => {
        this.navigateBill();
      })
    })
  }
  navigateShopping(){
    this.router.navigate(['shoes']);
  }
  navigateBill(){
    this.router.navigate(['bill']);
  }
  onDetail(idShoes: number){
    this.router.navigate(['dettail', idShoes]);
  }
  getImageURL(image: string){
    let objectURL = 'data:image/jpeg;base64,' + image;
    return this.sanitizer.bypassSecurityTrustUrl(objectURL);
  }
}
