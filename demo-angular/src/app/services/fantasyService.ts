import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { FantasyTeam } from '../common/fantasy/fantasyTeam';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class FantasyService {

  constructor(private httpClient : HttpClient) { }

  private baseUrl = 'http://localhost:8080/fantasy';

  public getFantasyTeamByUserId(userId: number):Observable<FantasyTeam> {
    return this.httpClient.get<FantasyTeam>(`${this.baseUrl}/fantasyTeam/${userId}`);
  }

 
}