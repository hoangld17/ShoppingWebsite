import { Subject } from 'rxjs';
import { BrandResponse } from './../model/brand-response.model';

export class BrandService{
  private brands: BrandResponse[] | null = null;
  brandChanged = new Subject<BrandResponse[]>();
  get getBrands() {
    return this.brands;
  }
  onAddBrands(brands: BrandResponse[]){
    this.brands = brands;
    this.brandChanged.next(this.brands);
  }
}
