import { BillDetailResponse } from "./bill-detail-response.model";

export class OneBillResponse{
  constructor(
    public id: number,
    public purchaseDate: string,
    public phone: string,
    public address: string,
    public total: number,
    public discount: number,
    public listBillDetail: BillDetailResponse[]
  ){}
}
