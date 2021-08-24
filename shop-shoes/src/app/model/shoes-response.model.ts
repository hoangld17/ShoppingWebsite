import { ShoeDetailResponse } from "./shoes-detail-response.model";

export class ShoesResponse{
  constructor(
    public id: number,
    public name: string,
    public price: number,
    public description: number,
    public discount: number,
    public image: string,
  ){}
}
