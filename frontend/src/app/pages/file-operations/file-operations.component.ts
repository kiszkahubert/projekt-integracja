import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { saveAs } from 'file-saver';
import { FileOperationsService } from '../../services/file-operations.service';

@Component({
  selector: 'app-file-operations',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './file-operations.component.html',
  styleUrls: ['./file-operations.component.scss']
})
export class FileOperationsComponent {
  constructor(private fileOperationsService: FileOperationsService) {}

  importFromJson(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const formData = new FormData();
    formData.append('file', file);

    this.fileOperationsService.importFromJson(formData).subscribe({
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
    this.fileOperationsService.exportToJson().subscribe({
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

    this.fileOperationsService.importFromXml(formData).subscribe({
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
    this.fileOperationsService.exportToXml().subscribe({
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