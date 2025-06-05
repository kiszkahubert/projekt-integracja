import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})

export class LoginComponent {
  errorMessage: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  handleLogin(creds: { username: string, password: string }) {
    if (!creds.username || !creds.password) {
      this.errorMessage = 'Username and password are required';
      return;
    }

    this.authService.login(creds.username, creds.password)
      .subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: (err) => {
          console.error(err);
          this.errorMessage = 'Login failed. Please try again.';
        }
      });
  }
}
