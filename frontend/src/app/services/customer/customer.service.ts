import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CustomerDTO } from 'src/app/models/customer-dto';
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
}
