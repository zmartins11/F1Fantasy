import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { FantasyTeam } from '../common/fantasy/fantasyTeam';
import { HttpClient } from '@angular/common/http';
import { Driverf } from '../common/fantasy/driverf';
import { Team } from '../common/fantasy/teamf';

@Injectable({
  providedIn: 'root'
})
export class FantasyService {


  constructor(private httpClient : HttpClient) { }

  fantasyTeam : any;
  
  private baseUrl = 'http://ec2-16-16-76-107.eu-north-1.compute.amazonaws.com/fantasy';
  //http://localhost:8080/fantasy/update
  private baseUrlLocal = 'http://localhost:8080/fantasy/';

  public getFantasyTeamByUserId(userId: number):Observable<FantasyTeam> {
    return this.httpClient.get<FantasyTeam>(`${this.baseUrl}/fantasyTeam/${userId}`);
  }

  public getAllDrivers():Observable<Driverf[]> {
    return this.httpClient.get<Driverf[]>(`${this.baseUrl}/drivers`);
  }

  public getAllConstructors():Observable<Team[]> {
    return this.httpClient.get<Team[]>(`${this.baseUrl}/teams`);
  }

    
  public updateTeam(fantasyTeam: FantasyTeam): Observable<FantasyTeam> {
    return this.httpClient.post<FantasyTeam>(`${this.baseUrl}/update`, fantasyTeam);
  }
 
}