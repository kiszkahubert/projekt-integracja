import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { saveAs } from 'file-saver';

@Injectable({
  providedIn: 'root'
})
export class FileOperationsService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  importFromJson(formData: FormData): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(`${this.baseUrl}/api/data/commodities/import-json`, formData, { 
      headers,
      observe: 'response'
    });
  }

  exportToJson(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    const startDateStr = '2022-01-01';
    const endDateStr = '2024-01-01';

    return this.http.get(`${this.baseUrl}/api/data/commodities/export?startDate=${startDateStr}&endDate=${endDateStr}`, { 
      headers,
      responseType: 'text',
      observe: 'response'
    });
  }

  importFromXml(formData: FormData): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(`${this.baseUrl}/api/data/conflicts/import-xml`, formData, { 
      headers,
      responseType: 'text',
      observe: 'response'
    });
  }

  exportToXml(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(`${this.baseUrl}/api/data/conflicts/export-xml`, { 
      headers,
      responseType: 'blob',
      observe: 'response'
    });
  }
} 