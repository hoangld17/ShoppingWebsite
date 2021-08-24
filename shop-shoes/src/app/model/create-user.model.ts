export class CreateUser{
  constructor(
    public firstName: string,
    public lastName: string,
    public email: string,
    public phone: string,
    public address: string,
    public username: string,
    public password: string,
    public confirmed: string
  ){}
}
