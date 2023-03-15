import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { FantasyTeam } from '../common/fantasy/fantasyTeam';
import { HttpClient } from '@angular/common/http';
import { Driverf } from '../common/fantasy/driverf';

@Injectable({
  providedIn: 'root'
})
export class FantasyService {


  constructor(private httpClient : HttpClient) { }

  fantasyTeam : any;
  
  private baseUrl = 'http://localhost:8080/fantasy';
  //http://localhost:8080/fantasy/update

  public getFantasyTeamByUserId(userId: number):Observable<FantasyTeam> {
    return this.httpClient.get<FantasyTeam>(`${this.baseUrl}/fantasyTeam/${userId}`);
  }

  public getAllDrivers():Observable<Driverf[]> {
    return this.httpClient.get<Driverf[]>(`${this.baseUrl}/drivers`);
  }

    
  public updateTeam(fantasyTeam: FantasyTeam): Observable<FantasyTeam> {
    return this.httpClient.post<FantasyTeam>(`${this.baseUrl}/update`, fantasyTeam);
  }
 
}