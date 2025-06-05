import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { Chart, registerables } from 'chart.js';
import { CommonModule } from '@angular/common';

Chart.register(...registerables);

@Component({
  selector: 'app-commodity-chart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './commodity-chart.component.html',
  styleUrls: ['./commodity-chart.component.scss']
})
export class CommodityChartComponent implements OnChanges {
  @Input() commodityData: any[] = [];
  @Input() conflictName: string = '';
  @Input() commodityName: string = '';
  private chart: Chart | null = null;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['commodityData'] || changes['conflictName'] || changes['commodityName']) {
      this.updateChart();
    }
  }

  private updateChart() {
    if (this.chart) {
      this.chart.destroy();
    }

    const ctx = document.getElementById('commodityChart') as HTMLCanvasElement;
    if (!ctx) return;

    const sortedData = [...this.commodityData].sort((a, b) => 
      new Date(a.date).getTime() - new Date(b.date).getTime()
    );

    const reductionFactor = Math.max(1, Math.floor(sortedData.length / 20));
    const reducedData = sortedData.filter((_, index) => index % reductionFactor === 0);

    const labels = reducedData.map(item => item.date);
    const prices = reducedData.map(item => item.price);

    this.chart = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: `${this.commodityName} prices during ${this.conflictName}`,
          data: prices,
          borderColor: 'rgb(75, 192, 192)',
          tension: 0,
          fill: false,
          pointRadius: 0,
          borderWidth: 2
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        scales: {
          y: {
            beginAtZero: false,
            title: {
              display: true,
              text: 'Price'
            }
          },
          x: {
            title: {
              display: true,
              text: 'Date'
            }
          }
        },
        plugins: {
          legend: {
            display: true,
            position: 'top'
          }
        }
      }
    });
  }
}
