import { AuthService } from './../service/auth.service';
import { AlertComponent } from './../alert/alert.component';
import { PlaceholderDirective } from './../alert/placeholder.directive';
import { ApiService } from './../service/api.service';

import { Component, ComponentFactoryResolver, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators, FormGroup, FormBuilder } from '@angular/forms';
import { Subscription } from 'rxjs';
import { LoginResponse } from '../model/login-response.model';
declare var $: any;

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css']
})
export class AuthComponent implements OnInit, OnDestroy {
  subs!: Subscription;
  closeSub!: Subscription;
  messageAlertSignIn: string | null = null;
  messageAlertSignUp: string | null = null;
  messageSuccessSignUp: string | null = null;
  autoLogin = true;
  @ViewChild(PlaceholderDirective) placeholderDirective!: PlaceholderDirective;
  constructor(private formBuilder: FormBuilder, private apiService: ApiService,
    private componentFactoryResolver: ComponentFactoryResolver){}
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }
  //Declare forms variable for actions
  registerForm!: FormGroup;
  submitted = false;

  loginForm!: FormGroup;
  loginsubmitted = false;


//Register form actions
get f() { return this.registerForm.controls; }
onRegisterSubmit() {
   this.submitted = true;
   // stop here if form is invalid
   if (this.registerForm.invalid) {
      return;
   }
   //True if all the fields are filled
   if(this.submitted)
   {
     this.apiService.signUpUser(this.registerForm.value).subscribe(res => {
      this.closeRegister();
      $("#loginmodal").modal("show");
      this.messageSuccessSignUp = res.data
     }, error => {
      console.log(error);
      this.messageAlertSignUp = error.error.message;

     });


   }

}
openRegister(){
  this.submitted = false;
  $("#loginmodal").modal("hide");
  $('#registermodal').modal({backdrop: 'static', keyboard: false})
  $("#registermodal").modal("show");
}
openLogin(){
  this.loginsubmitted = false;
  $("#registermodal").modal("hide");
  $('#loginmodal').modal({backdrop: 'static', keyboard: false});
  $("#loginmodal").modal("show");
}
closeRegister(){
  this.registerForm.reset();
  this.messageAlertSignUp = null;
  this.submitted = false;
  $("#registermodal").modal("hide");
}
closeLogin(){
  this.loginForm.reset();
  this.messageAlertSignIn = null;
  this.messageSuccessSignUp = null;
  this.loginsubmitted = false;
  $("#loginmodal").modal("hide");
}


// Login form actions
get ff() { return this.loginForm.controls; }
onLoginSubmit() {
   this.loginsubmitted = true;
   // stop here if form is invalid
   if (this.loginForm.invalid) {
       return;
   }
   //True if all the fields are filled
   if(this.loginsubmitted)
   {
     this.apiService.signInUser(this.loginForm.controls['username'].value, this.loginForm.controls['password'].value, this.autoLogin).subscribe(
       res => {
         console.log(res.data);
         this.closeLogin();
       },
       error => {
        console.log(error);
        this.messageSuccessSignUp = null;
        this.messageAlertSignIn = error.error.message;

       }
     );

   }
 }

  ngOnInit(){
 //Login form validations
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    });

 //Register form validations
    this.registerForm = this.formBuilder.group({
      firstName: ['', [Validators.required]],
      lastName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/), Validators.minLength(10)]],
      address: ['', [Validators.required]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmed: ['', [Validators.required, Validators.minLength(6)]],
    });
  }
  private showErrorAlert(message: string){
    const alertCmpFactory = this.componentFactoryResolver.resolveComponentFactory(AlertComponent);
    const hostViewContainerRef = this.placeholderDirective.viewContainerRef;
    hostViewContainerRef.clear();
    const componentRef = hostViewContainerRef.createComponent(alertCmpFactory);
    componentRef.instance.message = message;
    this.closeSub = componentRef.instance.close.subscribe(() => {
      this.closeSub.unsubscribe();
      hostViewContainerRef.clear();
    });
  }
  switchAutoLogin(){
    this.autoLogin = !this.autoLogin;
    console.log(this.autoLogin)
  }

}
