import { Component, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap/modal';
import { RaceInfo } from 'src/app/model/RaceInfo';

@Component({
  selector: 'app-race-popup',
  templateUrl: './race-popup.component.html',
  styleUrls: ['./race-popup.component.css']
})
export class RacePopupComponent implements OnInit {

  races!: RaceInfo[];
  round!: string;
  errorMessage!:string;

  constructor(public bsModalRef: BsModalRef)  { }


  ngOnInit(): void {
    this.round = this.bsModalRef.content.round;
    console.log("errorMESSA:" + this.errorMessage);
  }

  closePopup() {
    this.bsModalRef.hide();
  }

}
