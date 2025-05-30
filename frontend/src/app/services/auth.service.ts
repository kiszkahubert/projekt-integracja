import { Injectable } from "@angular/core";
import {HttpClient} from '@angular/common/http';
import {Observable, tap} from "rxjs";

interface LoginResponse{
    token: string;
    expiresIn: number;
}

interface RegisterDTO{
    username: string,
    password: string,
}

@Injectable({
    providedIn: 'root'
})

export class AuthService {
    constructor(private http: HttpClient) {}

    register(registerData: RegisterDTO): Observable<any>{
        return this.http.post('http://localhost:8080/auth/signup',registerData)
    }

    login(username: string, password: string): Observable<LoginResponse>{
        return this.http.post<LoginResponse>('http://localhost:8080/auth/login',{ username, password })
        .pipe(
            tap(response => {
                console.log('Odpowiedź z backendu:', response);
                localStorage.setItem('token', response.token);
                localStorage.setItem('expiresIn', response.expiresIn.toString());
            })
        );
    }

    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('expiresIn');
    }
    
    getToken(): string | null {
        if (typeof localStorage === 'undefined') {
            return null; // localStorage nie jest dostępny
        }
        return localStorage.getItem('token');
    }
    
    isAuthenticated(): boolean {
        const token = this.getToken();
        console.log('Token:', token);
        if (!token) return false;
        const expiresIn = localStorage.getItem('expiresIn');
        console.log('ExpiresIn:', expiresIn);
        if (!expiresIn) return false;
        const expirationDate = new Date(new Date().getTime() + parseInt(expiresIn));
        return expirationDate > new Date();
    }
}