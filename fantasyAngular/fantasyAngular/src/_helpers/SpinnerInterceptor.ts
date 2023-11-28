import { HttpEvent, HttpEventType, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse } from "@angular/common/http";
import { Injectable, Renderer2, RendererFactory2 } from "@angular/core";
import { NgxSpinnerService } from "ngx-spinner";
import { Observable, finalize, tap } from "rxjs";
import { SipnnerService } from "src/app/_services/SpinnerService";
import { DateTimeServiceService } from "src/app/_services/date-time-service.service";


@Injectable({
  providedIn: 'root'
})
export class SpinnerInterceptor implements HttpInterceptor {


  constructor(
    public spinnerHandler: SipnnerService
  ) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    this.spinnerHandler.handleRequest('plus');
    return next
      .handle(request)
      .pipe(
        finalize(this.finalize.bind(this))
      );
  }

  finalize = (): void => this.spinnerHandler.handleRequest();
}