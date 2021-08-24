export class BaseResponse<T> {
  constructor (public errorCode: number, public data: T, public message: string) {}
}
