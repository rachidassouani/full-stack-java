import { Component, Input, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
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

  operation: 'create' | 'update' = 'create';

  constructor(
    private customerService: CustomerService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService) {}

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
      if (this.operation === 'create') {
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
      } else if (this.operation === 'update') {
        this.customerService.updateCustomer(customerRegistartionRequest.id, customerRegistartionRequest)
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
                summary: 'Customer updaetd',
                detail: 'Customer updated successfully'
              })
              }
          })
      }
      
    }
  }

  deleteCustomer(customerDTO: CustomerDTO) {
    console.log(customerDTO);
    this.confirmationService.confirm({
      header: 'Delete Customer',
      message: `Are you sure you want to delete ${customerDTO.firstName}, Please note this operation cannot be undone`,
      accept: () => {
        if(customerDTO.id !== null && customerDTO.id !== undefined) {
          this.customerService.deleteCustomer(customerDTO.id).subscribe({
            
            next: () => {
              this.findAllCustomers();
              
              // display success notification to the user
              this.messageService.add({
                severity: 'success',
                summary: 'Customer deleted',
                detail: 'Customer deleted successfully'
              })
            
            }, error: () => {

            }
          });
        }
      }
    });    
  }


  updateCustomer(customerToUpdate: CustomerDTO) {
    this.operation = 'update';
    this.sidebarVisible = true;
    this.customer = customerToUpdate;
  }

  createCustomer() {
    this.operation = 'create';
    this.customer = {};
    this.sidebarVisible = true;
  }

  // cancel event received
  cancel() {
    this.operation = 'create';
    this.customer = {};
    this.sidebarVisible = false;
  }
}
