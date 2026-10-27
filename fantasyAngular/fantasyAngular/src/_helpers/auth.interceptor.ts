import {
  HttpEvent,
  HttpInterceptor,
  HttpHandler,
  HttpRequest,
  HttpErrorResponse
} from '@angular/common/http';

import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { Observable, catchError, throwError } from 'rxjs';

import { AuthService } from 'src/app/_services/auth.service';

const TOKEN_HEADER_KEY = 'Authorization';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {

    const token = this.authService.getToken();

    let authReq = req;

    if (token) {
      authReq = req.clone({
        headers: req.headers.set(
          TOKEN_HEADER_KEY,
          `Bearer ${token}`
        )
      });
    }

    return next.handle(authReq).pipe(

      catchError((error: HttpErrorResponse) => {

        console.log('HTTP ERROR STATUS:', error.status);
        console.log('HTTP ERROR URL:', error.url);
         console.log('HTTP ERROR:', error);

        if (error.status === 401 && token) {

          console.warn(
            'Unauthorized request. Redirecting to login.'
          );

          this.authService.signOut();

          this.router.navigate(['/login']);
        }

        return throwError(() => error);
      })
    );
  }
}