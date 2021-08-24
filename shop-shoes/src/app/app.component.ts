import { AuthService } from './service/auth.service';
import { ApiService } from './service/api.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  constructor(private authService: AuthService, private apiService: ApiService){}
  ngOnInit(): void {
    if (this.authService.autoLogin()){
      this.apiService.getQuantityCart().subscribe();
    }
  }

}
