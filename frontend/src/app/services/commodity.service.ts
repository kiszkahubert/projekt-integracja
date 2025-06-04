import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommodityService {
  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getCommodityTypes(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(`${this.baseUrl}/api/data/commodities/types/all`, { headers });
  }

  getCommodityData(commodityTypeId: number, startDate: string, endDate: string): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(
      `${this.baseUrl}/api/data/commodities/${commodityTypeId}?startDate=${startDate}&endDate=${endDate}`,
      { headers }
    );
  }
} 