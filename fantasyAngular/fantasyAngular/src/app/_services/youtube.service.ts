import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private http: HttpClient) { }

  baseUrl = ' http://127.0.0.1:5000/video-search'; 

  searchVideo(query: string): Observable<any> {
    const payload = { query };

    return this.http.post(this.baseUrl, payload);
  }
}
