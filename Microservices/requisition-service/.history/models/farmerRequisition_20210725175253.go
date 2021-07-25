package models

import (
	"time"
)

//User details
type FarmerRequisition struct {
	ID                   uint       `json:"id" gorm:"primary_key";"AUTO_INCREMENT"`
	ProductType          string     `json:"productType"`
	Quantity             int        `json:"quantity"`
	ExpectedDeliveryDate string     `json:"expectedDeliveryDate"`
	SpecialInstructions  string     `json:"specialInstructions";sql:"-"`
	PickupLocation       string     `json:"pickupLocation"`
	CreatorId            uint       `json:"creatorId"`
	Status               string     `json:"status"`
	CreatedAt            time.Time  `json:"createdAt"`
	UpdatedAt            time.Time  `json:"updatedAt"`
	DeletedAt            *time.Time `json:"deletedAt"`
}

//CreateUserInput fields
type CreateFarmerRequisitionInput struct {
	ProductType          string `json:"productType"`
	Quantity             int    `json:"quantity"`
	ExpectedDeliveryDate string `json:"expectedDeliveryDate"`
	SpecialInstructions  string `json:"specialInstructions";sql:"-"`
	CreatorId            uint   `json:"creatorId"`
	Status               string `json:"status"`
}
