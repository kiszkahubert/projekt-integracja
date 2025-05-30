import { Component } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  handleRegister(creds: { username: string, password: string, confirmPassword: string }) {
    if (creds.password !== creds.confirmPassword) {
      this.errorMessage = 'Passwords do not match';
      return;
    }

    if (!creds.username || !creds.password || !creds.confirmPassword) {
      this.errorMessage = 'Username and password are required';
      return;
    }

    this.errorMessage = '';
    this.authService.register({
      username: creds.username,
      password: creds.password
    }).subscribe({
      next: () => {
        this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error(err);
        this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
      }
    });
  }
}