import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../common/user';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(private httpClient : HttpClient) { }

  public loginUserFromRemote(user: User):Observable<any> {
    return this.httpClient.post<any>("http://localhost:8080/login", user);
  }

  public registerUserFromRemote(user : User):Observable<any> {
    return this.httpClient.post<any>("http://localhost:8080/registerUser", user);
  }
}
