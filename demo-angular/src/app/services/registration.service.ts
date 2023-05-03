import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { User } from '../common/user';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  private userSubject: BehaviorSubject<User> = new BehaviorSubject<User>({
    id: 0,
    emailId: "",
    userName: "",
    password: ""
  });

  setUser(user: User): void {
    this.userSubject.next(user);
  }

  getUser(): Observable<User> {
    return this.userSubject.asObservable();
  }


  constructor(private httpClient : HttpClient) { }

  public loginUserFromRemote(user: User):Observable<any> {
    return this.httpClient.post<any>("http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/login", user);
  }

  public registerUserFromRemote(user : User):Observable<any> {
    return this.httpClient.post<any>("http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/registerUser", user);
  }
}
