import { Component, OnInit } from '@angular/core';
import { Tech } from '../services/techstack/tech';
import { TechstackService } from '../services/techstack/techstack.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  title = 'biniko.me';
  techstack: Array<Tech> = [];

  constructor(private techstackService: TechstackService) { }

  ngOnInit() {
    this.techstack = this.techstackService.getTechstack();
  }

}
