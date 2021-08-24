export class BillUserResponse{
  constructor(
    public id: number,
    public purchaseDate: Date,
    public total: number
  ){}
}
