import { ApiService } from './api.service';
import { ShoesService } from './shoes.service';
import { ShoesResponse } from '../model/shoes-response.model';

import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot } from "@angular/router";
import { Observable } from 'rxjs';
import { PageResponse } from "../model/page-response.model";
import { map } from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class ShoesResolverService implements Resolve<PageResponse<ShoesResponse[]>>{
  constructor(private shoesService: ShoesService, private apiService: ApiService){}
  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): PageResponse<ShoesResponse[]> | Observable<PageResponse<ShoesResponse[]>> | Promise<PageResponse<ShoesResponse[]>> {
    if (this.shoesService.getShoes) {
      return this.shoesService.getShoes;
    } else {
      return this.apiService.fetchShoes().pipe(map(x => x.data));
    }
  }



}
