import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  private _eventsUrl = 'http://localhost:3000/api/events';
  private _specialEventsUrl = 'http://localhost:3000/api/special';

  constructor(private _httpClient: HttpClient) { }

  getEvents() {
    return this._httpClient.get<any>(this._eventsUrl).pipe(map(res => {
      return res;
    }, err => {
      this.handleError(err);
    }));
  }

  getSpecial() {
    return this._httpClient.get<any>(this._specialEventsUrl)
  }

  private handleError(error) {
    return throwError(error || 'Internal error');
  }
}
