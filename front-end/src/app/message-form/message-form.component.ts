import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../message";
import {MessageService} from "../services/message.service";
import {FormControl, FormGroup} from "@angular/forms";
import {SocketService} from "../services/socket.service";

@Component({
  selector: 'message-form',
  templateUrl: './message-form.component.html',
  styleUrls: ['./message-form.component.css']
})
export class MessageFormComponent implements OnInit {
  messageForm = new FormGroup({
    name: new FormControl(''),
    content: new FormControl(''),
  });

  @Input() messages: Message[]
  hasError: boolean = false

  constructor(private messageService: MessageService, private socketService: SocketService) { }

  ngOnInit(): void {
  }

  sendMessage(): void {
    let name = this.messageForm.value.name
    let content = this.messageForm.value.content

    console.log(this.messageForm.value);

    if ( !name || !content ) {
      this.hasError = true;
      return;
    }

    let message =  { name, content } as Message

    this.socketService.ws.next( message );

    this.messageService.addMessage(message).subscribe(message => this.messages.push(message) );

    this.hasError = false;
    this.messageForm.reset();
  }

}
