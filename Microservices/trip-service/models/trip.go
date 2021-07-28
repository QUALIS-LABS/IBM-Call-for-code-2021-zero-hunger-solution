package models

import (
	"time"
)

//Problem details
type Trip struct {
	ID                  uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	DriverID            int        `json:"driverId"`
	TripName            string     `json:"tripName"`
	TraderRequisitionID uint       `json:"traderRequisitionID"`
	FarmerRequisitionID uint       `json:"farmerRequisitionId`
	Status              string     `json:"status"`
	CreatedAt           time.Time  `json:"createdAt"`
	UpdatedAt           time.Time  `json:"updatedAt"`
	DeletedAt           *time.Time `json:"deletedAt"`
}

//CreateProblemInput fields
type CreateTripInput struct {
	DriverID            int    `json:"driverId"`
	TripName            string `json:"tripName"`
	TraderRequisitionID uint   `json:"traderRequisitionID"`
	FarmerRequisitionID uint   `json:"farmerRequisitionId`
	Status              string `json:"status"`
}
