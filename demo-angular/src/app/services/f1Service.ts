import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { map, Observable } from "rxjs";
import { Driver } from "../common/driver";
import { Race } from "../common/race";

@Injectable({
    providedIn: 'root'
})
export class f1Service {

    private baseUrl = "http://localhost:8080/rawData";

    wikipediaBaseUrl = 'https://en.wikipedia.org/w/api.php';
    private wikipediaUrl = 'https://en.wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&titles=';

    constructor(private httpClient: HttpClient) { }


    getRaces(): Observable<Race[]> {
        return this.httpClient.get<Race[]>("http://localhost:8080/2022");
    }

    getDriversList(season: number): Observable<Driver[]> {
        const search = `${this.baseUrl}/${season}`;
        return this.httpClient.get<Driver[]>(search);
    }

    getDriverImageExemplo(driverName: string): Observable<any> {
        const params = new HttpParams()
            .set('action', 'query')
            .set('format', 'json')
            .set('prop', 'pageimages')
            .set('titles', driverName)
            .set('pithumbsize', '500');

        return this.httpClient.get<{ query: { pages: { [key: string]: { thumbnail: { source: string } } } } }>(this.wikipediaBaseUrl, { params })
            .pipe(
                map((response: { query: { pages: any; }; }) => {
                    const page = response.query.pages;
                    const pageId = Object.keys(page)[0];
                    return page[pageId].thumbnail.source;
                })
            );
    }


    getDriverImage(driver: any) {
        const givenName = driver.givenName.split(' ').join('_');
        const familyName = driver.familyName.split(' ').join('_');
        const url = `https://en.wikipedia.org/w/api.php?action=query&format=json&prop=pageimages&piprop=original&titles=${givenName}_${familyName}`;
    
        return this.httpClient.get<any>(url).pipe(
          map(res => res['query']['pages'])
        );
      }
}
