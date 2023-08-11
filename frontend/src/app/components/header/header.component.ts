import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {

  items!: MenuItem[];

  ngOnInit() {
    this.items = [
      {label: 'Profile', icon:'pi pi-user'},
      {label: 'Settings', icon:'pi pi-cog'},
      {separator: true},
      {label: 'Sign out', icon:'pi pi-sign-out'}
    ];
  }
}
