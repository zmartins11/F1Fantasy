import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class YoutubeService {

  constructor(private http: HttpClient) { }

  private baseUrl = 'http://localhost:8000/video_search'; 

  searchVideo(query: string): Observable<any> {
    const url = `${this.baseUrl}/search`; // Adjust the endpoint if needed
    const payload = { query };

    return this.http.post(url, payload);
  }
}
