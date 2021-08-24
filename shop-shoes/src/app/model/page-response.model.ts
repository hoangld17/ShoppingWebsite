export class PageResponse<T> {
  constructor (public content: T, public totalPages: number, public number: number, public totalElements: number) {}
}
