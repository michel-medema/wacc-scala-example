import { Injectable } from '@angular/core';
import { webSocket } from "rxjs/webSocket";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class SocketService {
  private host = environment.host;
  private port = environment.port;

  constructor() {}

  ws = webSocket("ws://" + this.host + ":" + this.port + "/socket.io");
}
