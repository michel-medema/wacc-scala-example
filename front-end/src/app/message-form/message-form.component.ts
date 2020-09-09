import {Component, Input, OnInit} from '@angular/core';
import {Message} from "../message";
import {MessageService} from "../message.service";
import {FormControl, FormGroup} from "@angular/forms";

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

  constructor(private messageService: MessageService) { }

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

    this.messageService.addMessage({ name, content } as Message).subscribe(message => this.messages.push(message) );
    // TODO: Use web socket.

    this.hasError = false;
    this.messageForm.reset();
  }

}
