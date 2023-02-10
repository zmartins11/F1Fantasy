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
export class DriversListComponent implements OnInit, AfterViewInit{


  @Input()
  season!: number;

  dataSource: MatTableDataSource<Driver> = new MatTableDataSource;
  constructor(private f1Service: f1Service) { }
  selectedSeason!: number;

  drivers: Driver[] = [];
  seasons = [2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009, 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022];


  @ViewChild(MatSort)
  sort: MatSort = new MatSort;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  displayedColumns: string[] = ['image','familyName', 'givenName', 'nationality'];

  ngOnInit(): void {
    this.season = 2022;
    this.f1Service.getDriversList(this.season).subscribe(drivers => {
      this.dataSource = new MatTableDataSource(drivers);
      this.dataSource.paginator = this.paginator;
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    
  }

  doFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  getDrivers(season: number) {
    this.selectedSeason = season;
    this.f1Service.getDriversList(season).subscribe(data => {
      this.drivers = data;
      this.dataSource = new MatTableDataSource(this.drivers);
     
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
