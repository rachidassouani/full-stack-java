import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationRequest } from 'src/app/models/authentication-request';
import { AuthenticationResponse } from 'src/app/models/authentication-response';
import { CustomerRegistartionRequest } from 'src/app/models/customer-registartion-request';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { CustomerService } from 'src/app/services/customer/customer.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent {

  errorMessage = ''
  customerRegistartionRequest: CustomerRegistartionRequest = {};

  constructor(
    private router: Router, 
    private customerService: CustomerService,
    private authenticationService: AuthenticationService) {}

  onLogin() {
    this.router.navigate(['login'])
  }

  onRegisterAccount() {
    this.customerService.saveCustomer(this.customerRegistartionRequest)
      .subscribe({
        next: () => {
          const authRequest: AuthenticationRequest = {
            email: this.customerRegistartionRequest.email,
            password: this.customerRegistartionRequest.password
          }
          this.authenticationService.login(authRequest)
            .subscribe({
              next: (res) => {
                localStorage.setItem('user', JSON.stringify(res));
                this.router.navigate(['customers'])
              
              }, error: (e) => {
                if (e.error.statusCode == 401) {
                  this.errorMessage = e.error.message
                }
              }
            })
        }
      });
  }
}
