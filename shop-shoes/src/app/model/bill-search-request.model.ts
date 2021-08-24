export class BillSearchRequest{
  constructor(
    public latest: boolean,
    public page: number,
    public fromDate?: string,
    public toDate?: string
  ){}
}
