import { Component, OnInit } from '@angular/core';
import {Message} from "../message";
import {MessageService} from "../message.service";

@Component({
  selector: 'message-list',
  templateUrl: './message-list.component.html',
  styleUrls: ['./message-list.component.css']
})
export class MessageListComponent implements OnInit {
  messages: Message[]

  constructor(private messageService: MessageService) {}

  ngOnInit(): void {
    this.getMessages();
  }

  getMessages(): void {
    this.messageService.getMessages().subscribe( messages => this.messages = messages );
  }

}
