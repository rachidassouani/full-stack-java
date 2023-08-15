import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerRegistartionRequest } from 'src/app/models/customer-registartion-request';

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.scss']
})
export class ManageCustomerComponent {

  @Input()
  customer: CustomerRegistartionRequest = {};

  @Output()
  submit: EventEmitter<CustomerRegistartionRequest> = new EventEmitter();

  isCustomerValid(): boolean {
    return this.isInputValid(this.customer.firstName)
          && this.isInputValid(this.customer.lastName)
          && this.isInputValid(this.customer.email)
          && this.isInputValid(this.customer.password)
  }

  private isInputValid(input: string | undefined): boolean {
    return input !== null && input !== undefined && input.length > 0
  }

  onSubmit() {
    this.submit.emit(this.customer);
  }
}
