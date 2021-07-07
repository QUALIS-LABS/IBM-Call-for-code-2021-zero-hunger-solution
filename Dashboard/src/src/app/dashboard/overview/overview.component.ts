import { Component, OnInit } from "@angular/core";
import { ChartDataSets, ChartOptions } from 'chart.js';
import { Color, Label } from 'ng2-charts';

@Component({
        selector: 'app-overvi',
        templateUrl: './overview.component.html',
        styleUrls:['./overview.component.css']
      })

export class OverviewComponent implements OnInit{

        public chartType: string = 'line';

        public chartDatasets: ChartDataSets[] = [
        { data: [65, 59, 80, 81, 56, 55, 40, 76, 80, 50, 21, 23], label: 'Requisitions' },
        { data: [5, 9, 8, 3, 15, 3, 7, 6, 9, 2, 3, 6], label: 'Trips'},
        { data: [25, 19, 13, 5, 3, 10, 20, 21, 8, 1, 4, 7], label: 'Issues'}
        ];

        public chartLabels: Label[] = 
        ['January', 'February', 'March', 'April', 'May', 'June', 
        'July', 'August', 'September', 'October', 'November', 'December'];

        public chartColors: Color[] = [
        {
                backgroundColor: 'rgba(105, 0, 132, .2)',
                borderColor: 'rgba(200, 99, 132, .7)',
                borderWidth: 2,
        },
        {
                backgroundColor: 'rgba(0, 137, 132, .2)',
                borderColor: 'rgba(0, 10, 130, .7)',
                borderWidth: 2
        },
        {
                backgroundColor: 'rgba(137, 18, 0, .2)',
                borderColor: 'rgba(252, 141, 5, .7)',
                borderWidth: 2
        }];

        public chartOptions: ChartOptions = {
        responsive: true
        };

        lineChartLegend = true;

        lineChartPlugins = [];
        
        ngOnInit(){
        }
      
       
}