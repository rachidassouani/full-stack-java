import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable } from 'rxjs';
import { AuthenticationResponse } from 'src/app/models/authentication-response';

@Injectable({
  providedIn: 'root'
})
export class AccessGuardService implements CanActivate {

  constructor(private router: Router) { }


  canActivate(
    route: ActivatedRouteSnapshot, 
    state: RouterStateSnapshot): boolean | UrlTree | Observable<boolean | UrlTree> | Promise<boolean | UrlTree> {

      const loggedUser = localStorage.getItem('user');
      
      if (loggedUser) {
        const authReponse: AuthenticationResponse = JSON.parse(loggedUser);
        const token = authReponse.token;
        if (token) {
          const jwtHealper = new JwtHelperService();
          const isTokenNotExpired = !jwtHealper.isTokenExpired(token);
          if (isTokenNotExpired) {
            return true;
          }
        }
      }
      this.router.navigate(['login'])
      return false;
  }
}
