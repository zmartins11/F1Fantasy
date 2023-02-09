import { Component, Input, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Driver } from 'src/app/common/driver';
import { f1Service } from 'src/app/services/f1Service';

@Component({
  selector: 'app-drivers-list',
  templateUrl: './drivers-list.component.html',
  styleUrls: ['./drivers-list.component.css']
})
export class DriversListComponent implements OnInit {


  @Input()
  season!: number;

  dataSource!: MatTableDataSource<Driver>;
  constructor(private f1Service: f1Service) { }
  selectedSeason!: number;
  page = 1;
  searchTerm!: string;
  driverImageUrl: any;

  drivers: Driver[] = [];
  seasons = [2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022];

  filteredDrivers!: Driver[];

  
  displayedColumns: string[] = ['familyName', 'givenName', 'image'];

  ngOnInit(): void {
    
    this.f1Service.getDriversList(this.season).subscribe(drivers => {
      this.dataSource = new MatTableDataSource(drivers);
      console.log(season);
    });
  }

  searchDrivers() {
    this.filteredDrivers = this.drivers.filter(driver =>
      driver.familyName.toLowerCase().includes(this.searchTerm.toLowerCase()) ||
      driver.givenName.toLowerCase().includes(this.searchTerm.toLowerCase())
    );
  }

  getDrivers(season: number) {
    this.selectedSeason = season;
    this.f1Service.getDriversList(season).subscribe(data => {
      this.drivers = data;
      this.filteredDrivers = this.drivers;
    })

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
