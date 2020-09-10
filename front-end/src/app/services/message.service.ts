import { Injectable } from '@angular/core';
import {Message} from "../message";
import {Observable, of, throwError} from "rxjs";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {catchError, tap} from "rxjs/operators";

import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private API_URL= environment.API_URL;
  private messageUrl = this.API_URL + 'api/messages';

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

  constructor(private http: HttpClient) { }

  getMessages(): Observable<Message[]> {
    return this.http.get<Message[]>(this.messageUrl)
      .pipe(
        catchError(this.handleError<Message[]>('getMessages', []))
      );
  }

  addMessage(message: Message): Observable<Message> {
    return this.http.post<Message>(this.messageUrl, message, this.httpOptions).pipe(
      tap((newMessage: Message) => console.log(`added message w/ ${newMessage}`)),
      catchError(this.handleError<Message>('addMessage'))
    );
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {

      // TODO: perform better error handling.
      console.error(error);

      // Let the app keep running by returning an empty result.
      // Return an observable with a user-facing error message.
      return throwError('Something bad happened; please try again later.');
    };
  }
}
