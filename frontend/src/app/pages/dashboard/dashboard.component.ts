import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from '../navbar/navbar.component';
import { ConflictService } from '../../services/conflict.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-dashboard',
  imports: [NavbarComponent, CommonModule, FormsModule],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {
  conflicts: any[] = [];
  selectedConflict: any;

  constructor(
    private conflictService: ConflictService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadConflicts();
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

  onConflictChange() {
    console.log('Selected conflict:', this.selectedConflict);
  }
}
