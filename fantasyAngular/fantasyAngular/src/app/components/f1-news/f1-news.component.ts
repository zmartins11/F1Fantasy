import { Component, OnInit } from '@angular/core';
import { F1NewsService } from 'src/app/_services/f1-news.service';
import { News } from 'src/app/model/News';

@Component({
  selector: 'app-f1-news',
  templateUrl: './f1-news.component.html',
  styleUrls: ['./f1-news.component.css']
})
export class F1NewsComponent implements OnInit {

  news: News[] = [];
  loading: boolean = true;
  error: boolean = false;

  constructor(
    private f1NewsService: F1NewsService
  ) {}

  ngOnInit(): void {
    this.loadNews();
  }

  private loadNews(): void {
    this.loading = true;
    this.error = false;

    this.f1NewsService.getLatestNews().subscribe({
      next: (data: News[]) => {
        this.news = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading F1 news:', err);
        this.error = true;
        this.loading = false;
      }
    });
  }
}