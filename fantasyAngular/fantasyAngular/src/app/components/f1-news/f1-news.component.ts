import { AfterViewInit, Component, ElementRef, Renderer2 } from '@angular/core';

declare global {
  interface Window {
    twttr?: {
      widgets?: {
        load: (element?: HTMLElement) => void;
      };
      ready?: (callback: (twitter: NonNullable<Window['twttr']>) => void) => void;
    };
  }
}

@Component({
  selector: 'app-f1-news',
  templateUrl: './f1-news.component.html',
  styleUrls: ['./f1-news.component.css']
})
export class F1NewsComponent implements AfterViewInit {
  private static widgetsScript?: Promise<void>;

  constructor(private elementRef: ElementRef<HTMLElement>, private renderer: Renderer2) { }

  ngAfterViewInit(): void {
    this.loadTwitterWidgets();
  }

  private loadTwitterWidgets(): void {
    if (window.twttr?.widgets) {
      this.renderTwitterTimeline();
      return;
    }

    F1NewsComponent.widgetsScript ??= new Promise<void>((resolve, reject) => {
      const existingScript = document.querySelector('script[src="https://platform.twitter.com/widgets.js"]');
      if (existingScript) {
        existingScript.addEventListener('load', () => resolve());
        existingScript.addEventListener('error', () => reject());
        return;
      }

      const script = this.renderer.createElement('script') as HTMLScriptElement;
      script.src = 'https://platform.twitter.com/widgets.js';
      script.async = true;
      script.charset = 'utf-8';
      script.onload = () => resolve();
      script.onerror = () => reject();
      this.renderer.appendChild(document.body, script);
    });

    F1NewsComponent.widgetsScript
      .then(() => this.renderTwitterTimeline())
      .catch(() => undefined);
  }

  private renderTwitterTimeline(): void {
    const twitter = window.twttr;
    if (twitter?.widgets) {
      twitter.widgets.load(this.elementRef.nativeElement);
    } else if (twitter?.ready) {
      twitter.ready((readyTwitter) => readyTwitter.widgets?.load(this.elementRef.nativeElement));
    }
  }
}