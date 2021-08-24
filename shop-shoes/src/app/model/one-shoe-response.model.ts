import { ShoeDetailResponse } from "./shoes-detail-response.model";

export class OneShoeResponse{
  constructor(
    public id: number,
    public name: string,
    public idBrand: number,
    public nameBrand: string,
    public price: number,
    public description: string,
    public images: any[],
    public discount: number,
    public shoeDetailResponses: ShoeDetailResponse[]) {

  }
}
