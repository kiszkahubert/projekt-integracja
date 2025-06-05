import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConflictService {
  private baseUrl = '';

  constructor(private http: HttpClient) { }

  getConflicts(): Observable<any> {
    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Content-Type': 'text/xml;charset=UTF-8',
      'Authorization': `Bearer ${token}`
    });

    const soapRequest = `
      <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                        xmlns:int="http://kiszka.com/integracja">
        <soapenv:Header/>
        <soapenv:Body>
          <int:getConflictsRequest/>
        </soapenv:Body>
      </soapenv:Envelope>
    `;

    return this.http.post(`${this.baseUrl}/ws`, soapRequest, {
      headers,
      responseType: 'text'
    });
  }
}
