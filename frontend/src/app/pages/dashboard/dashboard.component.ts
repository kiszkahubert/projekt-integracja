import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { ConflictService } from '../../services/conflict.service';
import { CommodityService } from '../../services/commodity.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommodityChartComponent } from '../commodity-chart/commodity-chart.component';
import { FileOperationsComponent } from '../file-operations/file-operations.component';

@Component({
  selector: 'app-dashboard',
  imports: [
    NavbarComponent, 
    CommonModule, 
    FormsModule, 
    CommodityChartComponent,
    FileOperationsComponent
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  conflicts: any[] = [];
  selectedConflict: any;
  commodityTypes: any[] = [];
  selectedCommodityType: any;
  commodityData: any[] = [];
  showChart: boolean = false;

  constructor(
    private conflictService: ConflictService,
    private commodityService: CommodityService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadConflicts();
    this.loadCommodityTypes();
  }

  loadConflicts() {
    this.conflictService.getConflicts().subscribe({
      next: (response) => {
        const parser = new DOMParser();
        const xmlDoc = parser.parseFromString(response, "text/xml");
        const conflicts = xmlDoc.getElementsByTagName("conflict");
        
        this.conflicts = Array.from(conflicts).map(conflict => ({
          id: conflict.getElementsByTagName("id")[0]?.textContent,
          name: conflict.getElementsByTagName("name")[0]?.textContent,
          startDate: conflict.getElementsByTagName("startDate")[0]?.textContent,
          endDate: conflict.getElementsByTagName("endDate")[0]?.textContent
        }));
      },
      error: (error) => {
        console.error('Error loading conflicts:', error);
        if (error.status === 403) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
        }
      }
    });
  }

  loadCommodityTypes() {
    this.commodityService.getCommodityTypes().subscribe({
      next: (types) => {
        this.commodityTypes = types;
      },
      error: (error) => {
        console.error('Error loading commodity types:', error);
        if (error.status === 403) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
        }
      }
    });
  }

  onConflictChange() {
    if (this.selectedConflict && this.selectedCommodityType) {
      this.loadCommodityData();
    }
  }

  onCommodityTypeChange() {
    if (this.selectedConflict && this.selectedCommodityType) {
      this.loadCommodityData();
    }
  }

  loadCommodityData() {
    if (!this.selectedConflict || !this.selectedCommodityType) return;

    const startDate = this.selectedConflict.startDate;
    const endDate = this.selectedConflict.endDate || new Date().toISOString().split('T')[0];

    this.commodityService.getCommodityData(
      this.selectedCommodityType.id,
      startDate,
      endDate
    ).subscribe({
      next: (data) => {
        this.commodityData = data;
        this.showChart = true;
      },
      error: (error) => {
        console.error('Error loading commodity data:', error);
        if (error.status === 403) {
          localStorage.removeItem('token');
          this.router.navigate(['/login']);
        }
      }
    });
  }
}
