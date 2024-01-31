import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private http: HttpClient) { }

  private baseApi = environment.apiPythonUrl;
  private baseUrl = `${this.baseApi}/video-search`;
  

  searchVideo(query: string): Observable<any> {
    const payload = { query };

    return this.http.post(this.baseUrl, payload);
  }
}
