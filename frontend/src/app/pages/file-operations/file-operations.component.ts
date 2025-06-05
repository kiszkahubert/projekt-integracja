import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { saveAs } from 'file-saver';

@Component({
  selector: 'app-file-operations',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-operations.component.html',
  styleUrls: ['./file-operations.component.scss']
})
export class FileOperationsComponent {
  private baseUrl = '';

  constructor(private http: HttpClient) {}

  importFromJson(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('file', file);

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.post(`${this.baseUrl}/api/data/commodities/import-json`, formData, {
      headers,
      observe: 'response'
    }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          alert('Commodity data imported successfully');
          window.location.reload();
        } else {
          alert('Error importing commodity data');
        }
      },
      error: (error) => {
        console.error('Error importing commodity data:', error);
        alert('Error importing commodity data');
      }
    });
  }

  exportToJson() {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const startDateStr = '2022-01-01';
    const endDateStr = '2024-01-01';

    this.http.get(`${this.baseUrl}/api/data/commodities/export?startDate=${startDateStr}&endDate=${endDateStr}`, {
      headers,
      responseType: 'text',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        if (response.status === 200 && response.body) {

          const blob = new Blob([response.body], { type: 'application/json' });
          saveAs(blob, 'commodities.json');
        } else {
          alert('Error exporting commodity data');
        }
      },
      error: (error) => {
        console.error('Error exporting commodity data:', error);
        if (error.status === 403) {
          alert('Access denied. Please check your authentication.');
        } else {
          alert('Error exporting commodity data. Please try again.');
        }
      }
    });
  }

  importFromXml(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('file', file);

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.post(`${this.baseUrl}/api/data/conflicts/import-xml`, formData, {
      headers,
      responseType: 'text',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        if (response.status === 200) {
          alert('Conflict data imported successfully');
          window.location.reload();
        } else {
          alert('Error importing conflict data');
        }
      },
      error: (error) => {
        console.error('Error importing conflict data:', error);
        alert('Error importing conflict data');
      }
    });
  }

  exportToXml() {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    this.http.get(`${this.baseUrl}/api/data/conflicts/export-xml`, {
      headers,
      responseType: 'blob',
      observe: 'response'
    }).subscribe({
      next: (response) => {
        if (response.status === 200 && response.body) {
          saveAs(response.body, 'conflicts.xml');
        } else {
          alert('Error exporting conflict data');
        }
      },
      error: (error) => {
        console.error('Error exporting conflict data:', error);
        alert('Error exporting conflict data');
      }
    });
  }
}
