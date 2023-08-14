import { Component, Input, OnInit } from '@angular/core';
import { CustomerDTO } from 'src/app/models/customer-dto';
import { CustomerService } from 'src/app/services/customer/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit{
  
  sidebarVisible = false
  allCustomers: CustomerDTO[] = []

  constructor(private customerService: CustomerService) {}

  ngOnInit() {
    this.findAllCustomers();
  }

  private findAllCustomers() {
    this.customerService.findAllCustomers().subscribe({
      next: (allCustomers) => {
        this.allCustomers = allCustomers;
      },error: (error) => {
      }
    });
  }
}
