package models

import (
	"time"

	"github.com/lib/pq"
)

//Trip details
type Trip struct {
	ID                  uint           `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	DriverID            int            `json:"driverId"`
	TripName            string         `json:"tripName"`
	TraderRequisitionID uint           `json:"traderRequisitionID"`
	FarmerRequisitionID pq.StringArray `json:"farmerRequisitionId gorm:"type:text[]"`
	Status              string         `json:"status"`
	CreatedAt           time.Time      `json:"createdAt"`
	UpdatedAt           time.Time      `json:"updatedAt"`
	DeletedAt           *time.Time     `json:"deletedAt"`
}

//CreateTripInput fields
type CreateTripInput struct {
	DriverID            int    `json:"driverId"`
	TripName            string `json:"tripName"`
	TraderRequisitionID uint   `json:"traderRequisitionID"`
	FarmerRequisitionID uint   `json:"farmerRequisitionId`
	Status              string `json:"status"`
}
