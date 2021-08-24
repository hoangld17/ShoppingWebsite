export class BillDetailResponse{
  constructor(
    public id: number,
    public quantity: number,
    public price: number,
    public idShoeDetail: number,
    public idShoes: number,
    public size: number,
    public nameShoes: string,
    public nameBrand: string,
    public image: string
  ){}
}
