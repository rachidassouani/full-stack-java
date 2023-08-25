import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO } from 'src/app/models/customer-dto';
import { CustomerRegistartionRequest } from 'src/app/models/customer-registartion-request';
import { CustomerUpdateRequest } from 'src/app/models/customer-update-request';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private readonly customerUrl = `${environment.api.baseUrl}/${environment.api.customerUrl}`;

  constructor(private httpClient: HttpClient) { }

  findAllCustomers(): Observable<CustomerDTO[]>{
    // send get request to the backend
    return this.httpClient.get<CustomerDTO[]>(this.customerUrl);
  }

  saveCustomer(customer: CustomerRegistartionRequest): Observable<void> {
    return this.httpClient.post<void>(this.customerUrl, customer);
  }

  deleteCustomer(customerId: number | undefined): Observable<void> {
    return this.httpClient.delete<void>(`${this.customerUrl}/${customerId}`);
  }

  updateCustomer(
      customerId: number | undefined, 
      customerUpdateRequest: CustomerUpdateRequest): Observable<void> {
        return this.httpClient.put<void>(`${this.customerUrl}/${customerId}`, customerUpdateRequest);
  }
}
