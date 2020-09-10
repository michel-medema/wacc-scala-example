import { Component, OnInit } from '@angular/core';
import {Message} from "../message";
import {MessageService} from "../services/message.service";
import {SocketService} from "../services/socket.service";

@Component({
  selector: 'message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {
  messages: Message[] = [];

  constructor(private messageService: MessageService, private socketService: SocketService) {}

  ngOnInit(): void {
    this.getMessages();
    this.subscribe();
  }

  subscribe(): void {
    // TODO: Handle exceptions.
    this.socketService.ws.subscribe( (message: Message) => this.messages.push(message) );
  }

  getMessages(): void {
    this.messageService.getMessages().subscribe( messages => this.messages = messages );
  }

}
