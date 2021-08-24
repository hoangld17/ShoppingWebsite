import { AlertComponent } from './../../alert/alert.component';
import { PlaceholderDirective } from './../../alert/placeholder.directive';
import { ApiService } from './../../service/api.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Component, OnInit, ComponentFactoryResolver, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
declare var $: any;
@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {
  changePassword: string | null = null;
  submitted = false;
  changePasswordForm!: FormGroup;
  closeSub!: Subscription;
  @ViewChild(PlaceholderDirective) placeholderDirective!: PlaceholderDirective;
  constructor(private formBuilder: FormBuilder, private apiService: ApiService,
    private componentFactoryResolver: ComponentFactoryResolver) { }

  ngOnInit(): void {
    this.changePasswordForm = this.formBuilder.group({
      currentPassword: ['', [Validators.required, Validators.minLength(6)]],
      newPassword: ['', [Validators.required, Validators.minLength(6)]],
      confirmed: ['', [Validators.required, Validators.minLength(6)]]
    });
  }
  get f() {return this.changePasswordForm.controls}
  onSubmit(){
    this.submitted = true;
    if (this.changePasswordForm.invalid){
      return;
    }
    const currentPassword = this.changePasswordForm.controls['currentPassword'].value;
    const newPassword = this.changePasswordForm.controls['newPassword'].value;
    const confirmed = this.changePasswordForm.controls['confirmed'].value;
    if (newPassword != confirmed){
      this.changePassword = 'Password and confirm password are not the same!';
      return;
    }
    this.apiService.changePassword(currentPassword, newPassword).subscribe(res => {
      this.showErrorAlert(res.data);
      this.close();
    }, error => {
      this.changePassword = error.error.message;

    })
  }
  close(){
    $('#changepasswordmodal').modal('hide');
    this.submitted = false;
    this.changePasswordForm.reset();
    this.changePassword = null;
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
}
