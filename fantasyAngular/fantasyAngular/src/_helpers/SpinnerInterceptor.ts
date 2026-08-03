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

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

  console.log("START:", request.url);

  this.spinnerHandler.handleRequest("plus");

  return next.handle(request).pipe(
    finalize(() => {
      console.log("END:", request.url);
      this.spinnerHandler.handleRequest();
    })
  );
}

  //finalize = (): void => this.spinnerHandler.handleRequest();
}