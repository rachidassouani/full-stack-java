import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { AuthenticationResponse } from 'src/app/models/authentication-response';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  items!: MenuItem[];

  constructor(private router: Router) {

  }

  ngOnInit() {
    this.items = [
      {label: 'Profile', icon:'pi pi-user'},
      {label: 'Settings', icon:'pi pi-cog'},
      {separator: true},
      {
        label: 'Sign out', 
        icon:'pi pi-sign-out' , 
        command: () => {
          localStorage.clear();
          this.router.navigate(['login']);
      }}
    ];
  }

  getUsername(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      if (authResponse && authResponse.customerDTO && authResponse.customerDTO.email) {
        return authResponse.customerDTO.email;
      }
    }
    return '--';
  }

  getUserRole(): string {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const authResponse: AuthenticationResponse = JSON.parse(storedUser);
      if (authResponse && authResponse.customerDTO && authResponse.customerDTO.roles) {
        return authResponse.customerDTO.roles[0];
      }
    }
    return '--';
  }
}
