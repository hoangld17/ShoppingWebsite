export class LoginResponse{
  constructor(
    public username: string,
    public firstName: string,
    public lastName: string,
    public email: string,
    public phone: string,
    public address: string,
    public token: string,
    public image: string,
  ){}
}
