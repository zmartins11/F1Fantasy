import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Driver } from 'src/app/common/driver';
import { f1Service } from 'src/app/services/f1Service';

@Component({
  selector: 'app-drivers-list',
  templateUrl: './drivers-list.component.html',
  styleUrls: ['./drivers-list.component.css']
})
export class DriversListComponent implements OnInit{

  

  familyName : any;
  season: any;
  p : number = 1;

  selectedSeason : any;

  dataSource: MatTableDataSource<Driver> = new MatTableDataSource;
  constructor(private f1Service: f1Service) { }

  drivers: Driver[] = [];
  seasons = [2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022];

  

  ngOnInit(): void {
    const currentYear = new Date().getFullYear() - 1;
    //to show the last season highlit
    this.selectedSeason = this.seasons[this.seasons.length - 1];
    this.season = currentYear.toString();
    this.f1Service.getDriversList(this.season).subscribe(drivers => {
      this.drivers = drivers;
    });
    
  }

  getDrivers(season: number) {
    this.f1Service.getDriversList(season).subscribe(data => {
      this.drivers = data;
      this.dataSource = new MatTableDataSource(this.drivers);
     this.selectedSeason = season;
    })

  }

  search(selectedSeason : number) {
    if(this.familyName == "") {
      this.getDrivers(selectedSeason);
    }  else {
      this.drivers = this.drivers.filter(res =>{
        return res.familyName.toLocaleLowerCase().match(this.familyName.toLocaleLowerCase());
      })
    }
  }


  getDriverImage(driver: Driver): string {
    const imageUrl = `assets/${driver.familyName}.jpg`;
    return this.checkImageExists(imageUrl) ? imageUrl : 'assets/default.png';
  }

  checkImageExists(url: string): boolean {
    let imageExists = false;
    const http = new XMLHttpRequest();
    http.open('HEAD', url, false);
    http.send();
    if (http.status === 200) {
      imageExists = true;
    }
    return imageExists;
  }



}
