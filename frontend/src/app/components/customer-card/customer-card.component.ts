import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerDTO } from 'src/app/models/customer-dto';

@Component({
  selector: 'app-customer-card',
  templateUrl: './customer-card.component.html',
  styleUrls: ['./customer-card.component.scss']
})
export class CustomerCardComponent {

  @Input()
  customer: CustomerDTO = {};

  @Output()
  delete: EventEmitter<CustomerDTO> = new EventEmitter();

  onDelete() {
    this.delete.emit(this.customer);
  }
}
