import { Component, Input, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { CustomerDTO } from 'src/app/models/customer-dto';
import { CustomerRegistartionRequest } from 'src/app/models/customer-registartion-request';
import { CustomerService } from 'src/app/services/customer/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit{
  
  sidebarVisible = false
  allCustomers: CustomerDTO[] = []
  customer: CustomerRegistartionRequest = {};

  constructor(
    private customerService: CustomerService,
    private messageService: MessageService) {}

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

  save(customerRegistartionRequest: CustomerRegistartionRequest) {
    if (customerRegistartionRequest) {
      this.customerService.saveCustomer(customerRegistartionRequest)
        .subscribe({
          next: () => {

            // fetch the list of customers in order to display our new saved customer
            this.findAllCustomers();
            
            // to close sidebar from
            this.sidebarVisible = false

            // clear the customer form
            this.customer = {}

            // display success notification to the user
            this.messageService.add({
              severity: 'success',
              summary: 'Customer saved',
              detail: 'Customer saved successfully'
            })
          }, error: () => {

          }
        })
    }
  }
}
