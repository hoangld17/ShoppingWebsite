import { ApiService } from './api.service';
import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";
import { LoginResponse } from "../model/login-response.model";

@Injectable({providedIn: 'root'})
export class AuthService{
  user = new BehaviorSubject<LoginResponse | null>(null);
  login(autoLogin: boolean, loginResponse: LoginResponse){
    if (autoLogin){
      this.user.next(loginResponse);
      localStorage.setItem('userData', JSON.stringify(loginResponse));
    } else {
      this.user.next(loginResponse);
    }
  }
  logout(){
    this.user.next(null);
    localStorage.removeItem('userData');
  }
  autoLogin(){
    const userStorage = localStorage.getItem('userData');
    if(userStorage){
      const loginData: LoginResponse = JSON.parse(userStorage!);
      this.user.next(loginData);
    }
    return userStorage!!;
  }
  updateAvatar(image: string){
    const userStorage = localStorage.getItem('userData');
    if(userStorage){
      const loginData: LoginResponse = JSON.parse(userStorage!);
      loginData.image = image;
      this.user.next(loginData);
      localStorage.removeItem('userData');
      localStorage.setItem('userData', JSON.stringify(loginData));
    }
  }
  updateInfoUser(infoUser: {firstName: string, lastName: string, email: string, phone: string, address: string}){
    const userStorage = localStorage.getItem('userData');
    if(userStorage){
      const loginData: LoginResponse = JSON.parse(userStorage!);
      if (infoUser.firstName.trim() != '')
        loginData.firstName = infoUser.firstName;

      if (infoUser.lastName.trim() != '')
        loginData.lastName = infoUser.lastName;

      if (infoUser.email.trim() != '')
        loginData.email = infoUser.email;

      if (infoUser.phone.trim() != '')
        loginData.phone = infoUser.phone;

      if (infoUser.address.trim() != '')
        loginData.address = infoUser.address;

      this.user.next(loginData);
      localStorage.removeItem('userData');
      localStorage.setItem('userData', JSON.stringify(loginData));
    }
  }
}
