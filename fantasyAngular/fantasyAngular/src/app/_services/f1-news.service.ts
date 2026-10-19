import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { News } from '../model/News';

@Injectable({
  providedIn: 'root'
})
export class F1NewsService {

  private readonly apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getLatestNews(): Observable<News[]> {
    return this.http.get<News[]>(`${this.apiUrl}/api/f1/news`);
  }
}