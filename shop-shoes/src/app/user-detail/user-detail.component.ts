import { ApiService } from './../service/api.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from './../service/auth.service';
import { DomSanitizer } from '@angular/platform-browser';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-user-detail',
  templateUrl: './user-detail.component.html',
  styleUrls: ['./user-detail.component.css']
})
export class UserDetailComponent implements OnInit, OnDestroy {
  image: any =null;
  fullName!: string;
  email!: string;
  messageSuccess: string | null = null;
  messageError: string | null = null;
  infoUser!: FormGroup;
  subs!: Subscription;
  constructor(private sanitizer: DomSanitizer, private authService: AuthService, private formBuilder: FormBuilder,
    private apiService: ApiService) { }
  ngOnDestroy(): void {
    this.subs.unsubscribe();
  }

  ngOnInit(): void {
    this.subs = this.authService.user.subscribe(res => {
      if (res) {
        this.fullName = res.firstName + ' ' + res.lastName;
        this.email = res.email;
        this.infoUser = this.formBuilder.group({
          firstName: [res.firstName, [Validators.required]],
          lastName: [res.lastName, [Validators.required]],
          email: [res.email, [Validators.required, Validators.email]],
          phone: [res.phone, [Validators.required, Validators.pattern(/^-?(0|[1-9]\d*)?$/), Validators.minLength(10)]],
          address: [res.address, [Validators.required]]
        });
        let objectURL = 'data:image/jpeg;base64,' + res.image;
        this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
      }
    })

  }
  importData() {
    let input = document.createElement('input');
    input.type = 'file';
    input.accept = '.jpg, .png, .jpeg, .gif, .bmp, .tif, .tiff|image/*'
    input.onchange = _ => {
      // you can use this method to get file and perform respective operations
      let files =   Array.from(input.files!);
      const reader = new FileReader();
      reader.readAsDataURL(files[0]);
      reader.onload = () => {
        const data = reader.result!.toString();
        const newImage = data.split(',', 2)[1];
        console.log(newImage);
        let objectURL = 'data:image/jpeg;base64,' + newImage;
        this.image = this.sanitizer.bypassSecurityTrustUrl(objectURL);
        this.apiService.updateAvatar(newImage).subscribe(res => {
          this.messageError = null;
          this.messageSuccess = res.data;
          setTimeout(() => {
            this.messageSuccess = null;
          }, 2000)
        }, error => {
          this.messageError = error.error.message;
        });
      };
    };
    input.click();
  }
  get f() {return this.infoUser.controls};

  onSubmit(){
    this.apiService.updateInfoUser(this.infoUser.value).subscribe(res => {
      this.messageError = null;
      this.messageSuccess = res.data;
      setTimeout(() => {
        this.messageSuccess = null;
      }, 2000)
    }, error => {
      this.messageError = error.error.message;
    });
  }
}
