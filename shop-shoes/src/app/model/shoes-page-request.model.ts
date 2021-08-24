export class ShoesPageRequest{
  constructor(
    public page: number,
    public size: number,
    public search?: string,
    public idBrands?: number[],
    public minPrice?: number,
    public maxPrice?: number,
    public sortField?: string,
    public sortType?: boolean
  ){}
}
