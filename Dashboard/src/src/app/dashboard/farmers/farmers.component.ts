import { Component, OnInit } from '@angular/core';

const ELEMENT_DATA = [
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
        {
                name: "John Doe",
                email: "johndoe@gmail.com",
                phone: "+254 711 344 555",
                requisitions: "659",
                rating: 3.5 
        },
      ];

@Component({
  selector: 'app-farmers',
  templateUrl: 'farmers.component.html',
  styleUrls:['farmers.component.css']
})

export class FarmersComponent implements OnInit {

        displayedColumns: string[] = ['name', 'email', 'phone', 'requisitions', 'rating'];
        dataSource = ELEMENT_DATA;

        ngOnInit() { }



}
